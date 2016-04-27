package org.ucomplex.ucomplex.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Adaptors.ProtocolAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Calendar.CalendarOneDay;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolActivity extends AppCompatActivity {

    private CalendarOneDay mCalendarOneDay;
    ListView mListView;
    LinearLayout linlaHeaderProgress;

    private int subjId;
    private String dayMonthYear;
    private int hourNumber;
    private ArrayList<Pair<Integer, Integer>> studentsIds = new ArrayList<>();

    ProtocolAdapter protocolAdapter;
    Spinner spinner;
    ArrayList<String> options = new ArrayList<>();
    String hourType;
    int hourTypeInt;

    public static ImageButton doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        doneButton = (ImageButton) findViewById(R.id.settings_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        HashMap<String, String> postParams = new HashMap<>();
                        postParams.put("subjId", String.valueOf(subjId));
                        postParams.put("time", dayMonthYear);
                        postParams.put("hourNumber", String.valueOf(hourNumber));
                        postParams.put("hourType", hourType);
                        for(int i = 0; i < studentsIds.size(); i++){
                            postParams.put("arr["+studentsIds.get(i).getValue0()+"][mark]", String.valueOf(studentsIds.get(i).getValue1()));
                        }
                        String url = "https://ucomplex.org/teacher/ajax/save_attendance?mobile=1";
                        String jsonString = Common.httpPost(url, Common.getLoginDataFromPref(ProtocolActivity.this), postParams);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Toast.makeText(ProtocolActivity.this, "Сохранено", Toast.LENGTH_LONG).show();

                    }
                }.execute();
            }
        });
        final Bundle extra = getIntent().getExtras();
        subjId = extra.getInt("subjId");
        dayMonthYear = extra.getString("dayMonthYear");
        hourNumber = extra.getInt("hourNumber");
        hourTypeInt = extra.getInt("hourType");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Занятие " + (hourNumber));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        mListView = (ListView) findViewById(R.id.subject_listview);

        spinner = (Spinner) findViewById(R.id.spinner_protocol);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        options.add("ЗАНЯТИЕ");
        options.add("АТТЕСТАЦИЯ");
        options.add("ЭКЗАМЕН");
        options.add("НИДИВИДУАЛЬНОЕ ЗАНЯТИЕ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_white, options.toArray(new String[options.size()])) {

            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);

                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(hourTypeInt);
        spinner.setGravity(Gravity.CENTER);
        hourType = options.get(hourTypeInt);
        AdapterView.OnItemSelectedListener subjectSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                String[] hourTypeArray = new String[]{"common_hour", "control_hour", "exam_hour", "work_off"};
                hourType = hourTypeArray[position];}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(subjectSelectedListener);

        new AsyncTask<String, Void, CalendarOneDay>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                linlaHeaderProgress.setVisibility(View.VISIBLE);
            }

            @Override
            protected CalendarOneDay doInBackground(String... params) {
                HashMap<String, String> postParams = new HashMap<>();
                postParams.put("subjId", String.valueOf(subjId));
                postParams.put("time", dayMonthYear);
                postParams.put("hourNumber", String.valueOf(hourNumber));
                String url = "https://ucomplex.org/teacher/ajax/oneday?mobile=1";
                String jsonString = Common.httpPost(url, Common.getLoginDataFromPref(ProtocolActivity.this), postParams);
                return getOneDay(jsonString);
            }

            @Override
            protected void onPostExecute(CalendarOneDay s) {
                super.onPostExecute(s);
                mCalendarOneDay = s;
                protocolAdapter = new ProtocolAdapter(ProtocolActivity.this, mCalendarOneDay.getStudentsProgress());
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        showInputDialog(position, protocolAdapter.getmItems().get(position).getValue1().toString());
                    }
                });
                mListView.setAdapter(protocolAdapter);
                linlaHeaderProgress.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    protected void showInputDialog(final int position, String oldName) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        editText.setText(oldName);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Common.isNetworkConnected(ProtocolActivity.this)) {
                            String newName = editText.getText().toString();
                             newName = newName.replace(" ","");
                            if(newName.equals("")){
                                newName = "0";
                            }
                                Triplet<Integer, Integer, String> triplet = protocolAdapter.getmItems().get(position);
                                triplet = triplet.setAt1(Integer.valueOf(newName));
                                protocolAdapter.setItemAt(triplet, position);
                                protocolAdapter.notifyDataSetChanged();
                                Pair<Integer, Integer> pair = new Pair(protocolAdapter.getItem(position).getValue0(), protocolAdapter.getItem(position).getValue1());
                                studentsIds.add(pair);

                        } else {
                            Toast.makeText(ProtocolActivity.this, "Проверте интернет соединение", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("Отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.setMessage(protocolAdapter.getmItems().get(position).getValue2()).create();
//            alert.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
        alert.show();
    }

    @Nullable
    private CalendarOneDay getOneDay(String jsonData) {
        JSONObject calendarDayJson;
        CalendarOneDay calendarOneDay = new CalendarOneDay();
        try {
            calendarDayJson = new JSONObject(jsonData);
            calendarOneDay.setSubjId(calendarDayJson.getInt("subjId"));
            calendarOneDay.setDate(calendarDayJson.getString("date"));
            calendarOneDay.setTime(calendarDayJson.getString("time"));
            calendarOneDay.setHourNumber(calendarDayJson.getInt("hourNumber"));
            try{
                calendarOneDay.setSubgroup(calendarDayJson.getInt("subgroup"));
            }catch (JSONException ignored){}
            calendarOneDay.setGroup(calendarDayJson.getInt("group"));
            calendarOneDay.setCourse(calendarDayJson.getInt("course"));
            calendarOneDay.setTable(calendarDayJson.getInt("table"));
            calendarOneDay.setMaxNumber(calendarDayJson.getInt("maxNumber"));
            try{
                calendarOneDay.setHourType(calendarDayJson.getInt("hourType"));
            }catch (JSONException ignored){}
            try{
                calendarOneDay.setRecordID(calendarDayJson.getInt("recordID"));
            }catch (JSONException ignored){}
            JSONObject studentProgressJson = new JSONObject();
            try{
                studentProgressJson = calendarDayJson.getJSONObject("student_progress");
            }catch (JSONException ignored){}
            JSONArray studentsJson = new JSONArray();
            try{
                studentsJson = calendarDayJson.getJSONArray("students");
            }catch (JSONException ignored){}

            ArrayList<Triplet<Integer, Integer, String>> studentsProgressArray = new ArrayList<>();

            for (int i = 0; i < studentsJson.length(); i++) {
                Integer studentId = -1;
                Integer mark = 0;
                String name = "-1";
                JSONObject studentJson = studentsJson.getJSONObject(i);
                try{
                    studentId = studentJson.getInt("id");
                }catch (JSONException ignored){}
                try{
                    mark = studentProgressJson.getInt(studentJson.getString("id"));
                }catch (JSONException ignored){}
                try{
                    name = studentJson.getString("name");
                }catch (JSONException ignored){}

                Triplet<Integer, Integer, String> studentInfo = new Triplet<>(studentId, mark, name);
                studentsProgressArray.add(studentInfo);
            }
            calendarOneDay.setStudentsProgress(studentsProgressArray);
            return calendarOneDay;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
