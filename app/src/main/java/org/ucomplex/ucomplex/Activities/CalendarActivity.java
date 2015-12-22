package org.ucomplex.ucomplex.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarTask;
import org.ucomplex.ucomplex.R;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        toolbar.setTitle("Календарь");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FetchCalendarTask fetchCalendarTask = new FetchCalendarTask();
        fetchCalendarTask.setmContext(this);
        fetchCalendarTask.execute("02","01.02.2015");
    }

}
