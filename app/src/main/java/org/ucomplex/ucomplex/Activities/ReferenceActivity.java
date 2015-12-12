package org.ucomplex.ucomplex.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.MyServices;
import org.ucomplex.ucomplex.R;

import java.util.HashMap;

public class ReferenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText text;
    private String referencePlace;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.reference_place_choice);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reference_places_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        this.text = (EditText) findViewById(R.id.reference_field);
        System.out.println();
        Button sendButton = (Button) findViewById(R.id.reference_send_button);
        sendButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SendReferenceTask sendReferenceTask = new SendReferenceTask();
                sendReferenceTask.setmContext(ReferenceActivity.this);
                sendReferenceTask.execute(text.getText().toString(),referencePlace);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.referencePlace = (String) parent.getItemAtPosition(position);
        System.out.println();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class SendReferenceTask extends AsyncTask<String, Void, Void>{

        Context mContext;

        public Context getmContext() {
            return mContext;
        }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected Void doInBackground(String... params) {
            String urlString = "http://you.com.ru/statics/student/verification?json";
            HashMap<String, String> postParams = new HashMap<>();
            postParams.put("organization", String.valueOf(params[0]));
            postParams.put("place", String.valueOf(params[1]));
            postParams.put("send_verif", String.valueOf(1));
            String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext), postParams);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final View coordinatorLayoutView = findViewById(R.id.reference_layout);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayoutView, "Ваша заявка принията!", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }
}
