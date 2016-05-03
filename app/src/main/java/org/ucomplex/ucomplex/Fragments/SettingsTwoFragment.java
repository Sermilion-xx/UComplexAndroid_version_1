package org.ucomplex.ucomplex.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.SettingsActivity2;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Sermilion on 29/02/16.
 */
public class SettingsTwoFragment extends Fragment {

    public static boolean STUDY_RANK_CHANGED;
    public static boolean STUDY_DEGREE_CHANGED;
    public static boolean STUDY_DEGREE_CHANGED_2;
    public static boolean DISCIPLINES_CHANGED;
    public static boolean UPQUALIFICATIONS_CHANGED;
    public static boolean BIO_CHANGED;

    private User user;

    private TextView disciplinesTextView;
    private TextView upqualificationsTextView;
    private TextView bioTextView;

    private String disciplines = "";
    private String upqualifications = "";
    private String bio = "";

    private Spinner spinnerRank;
    private Spinner spinnerDegree;
    private Spinner spinnerDegree_2;

    private int spinnerRankValue;
    private int spinnerDegreeValue;
    private int spinnerDegree_2Value;

    private ArrayList<String> optionsRank = new ArrayList<>();
    private ArrayList<String> optionsDegree = new ArrayList<>();
    private ArrayList<String> optionsDegree_2 = new ArrayList<>();

    private SettingsActivity2 mContext;

    public void setCustomInfoSettings(JSONObject customInfoSettings) {

        try {
            disciplinesTextView.setText(customInfoSettings.getString("courses"));
            upqualificationsTextView.setText(customInfoSettings.getString("upqualification"));
            bioTextView.setText(customInfoSettings.getString("bio"));

            spinnerRank.setSelection(customInfoSettings.getInt("rank"));
            int degree = customInfoSettings.getInt("degree");
            int degree1 = 0;
            int degree2 = Math.abs(degree);
            if(degree<0){
                degree1 = 0;
            }else if(degree>0){
                degree1 = 1;
            }
            spinnerDegree.setSelection(degree1);
            spinnerDegree_2.setSelection(degree2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setmContext(SettingsActivity2 mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_two, container, false);

        disciplinesTextView = (TextView) rootView.findViewById(R.id.settings_subjects_value);
        upqualificationsTextView = (TextView) rootView.findViewById(R.id.settings_upqualification_value);
        bioTextView = (TextView) rootView.findViewById(R.id.settings_bio_value);

        spinnerRank = (Spinner) rootView.findViewById(R.id.settings_rank_value);
        spinnerDegree = (Spinner) rootView.findViewById(R.id.settings_degree_value);
        spinnerDegree_2 = (Spinner) rootView.findViewById(R.id.settings_degree2_value);



        spinnerRank.setTag("rank");
        spinnerDegree.setTag("degree");
        spinnerDegree_2.setTag("degree_2");

        optionsDegree.add("Кандидат");
        optionsDegree.add("Доктор");

        optionsDegree_2.add("Выбрать");
        Collections.addAll(optionsDegree_2, Common.degrees);

        optionsRank.add("Выбрать");
        Collections.addAll(optionsRank, Common.ranks);

        ArrayAdapter<String> adapterRank = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, optionsRank.toArray(new String[optionsRank.size()]));
        adapterRank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> adapterDegree = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, optionsDegree.toArray(new String[optionsDegree.size()]));
        adapterDegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterDegree_2 = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, optionsDegree_2.toArray(new String[optionsDegree_2.size()]));
        adapterDegree_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRank.setAdapter(adapterRank);
        spinnerDegree.setAdapter(adapterDegree);
        spinnerDegree_2.setAdapter(adapterDegree_2);

        spinnerRank.setSelection(0);
        spinnerDegree.setSelection(0);
        spinnerDegree_2.setSelection(0);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (disciplinesTextView.getText().hashCode() == s.hashCode()) {
                    disciplines = disciplinesTextView.getText().toString();
                } else if (upqualificationsTextView.getText().hashCode() == s.hashCode()) {
                    upqualifications = upqualificationsTextView.getText().toString();
                } else if (bioTextView.getText().hashCode() == s.hashCode()) {
                    bio = bioTextView.getText().toString();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (disciplinesTextView.getText().hashCode() == s.hashCode()) {
                    DISCIPLINES_CHANGED = true;
                } else if (upqualificationsTextView.getText().hashCode() == s.hashCode()) {
                    UPQUALIFICATIONS_CHANGED = true;
                } else if (bioTextView.getText().hashCode() == s.hashCode()) {
                    BIO_CHANGED = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        disciplinesTextView.addTextChangedListener(textWatcher);
        upqualificationsTextView.addTextChangedListener(textWatcher);
        bioTextView.addTextChangedListener(textWatcher);

        AdapterView.OnItemSelectedListener subjectSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getTag().equals("rank")) {
                    spinnerRankValue = position;
                    STUDY_RANK_CHANGED = true;
                }
                if (parent.getTag().equals("degree")) {
                    spinnerDegreeValue = position;
                    STUDY_DEGREE_CHANGED = true;
                }
                if (parent.getTag().equals("degree_2")) {
                    spinnerDegree_2Value = position;
                    STUDY_DEGREE_CHANGED_2 = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinnerRank.setOnItemSelectedListener(subjectSelectedListener);
        spinnerDegree.setOnItemSelectedListener(subjectSelectedListener);
        spinnerDegree_2.setOnItemSelectedListener(subjectSelectedListener);


        return rootView;
    }


    public void saveSettingsTwo(){

        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> postParams = new HashMap<>();
                String urlString = "https://ucomplex.org/teacher/profile/save?mobile=1";
                postParams.put("courses", disciplines);
                postParams.put("rank", String.valueOf(spinnerRankValue));
                int degree;
                if(spinnerDegreeValue == 0){
                    degree = -spinnerDegree_2Value;
                }else{
                    degree = spinnerDegreeValue;
                }
                postParams.put("bio", bio);
                postParams.put("upqualification", upqualifications);
                postParams.put("degree", String.valueOf(degree));
                String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(getContext()),postParams);
                return jsonData;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                if(response!=null){
                    if(response.length()>0){
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            response = responseJson.getString("status");
                            if(response.equals("success")){
                                STUDY_RANK_CHANGED = false;
                                STUDY_DEGREE_CHANGED = false;
                                STUDY_DEGREE_CHANGED_2 = false;
                                DISCIPLINES_CHANGED = false;
                                UPQUALIFICATIONS_CHANGED = false;
                                BIO_CHANGED = false;
                                Toast.makeText(mContext, "Настройки сохранены", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.execute();
    }
}
