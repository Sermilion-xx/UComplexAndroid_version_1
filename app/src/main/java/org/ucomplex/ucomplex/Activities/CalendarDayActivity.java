package org.ucomplex.ucomplex.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import org.ucomplex.ucomplex.Adaptors.CalendarDayAdapter;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

public class CalendarDayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle((String) getIntent().getExtras().get("date"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList calendarDay = (ArrayList) getIntent().getExtras().get("calendarDay");
        ListView listView = (ListView) findViewById(R.id.calendar_day_listview);
        CalendarDayAdapter calendarDayAdapter = new CalendarDayAdapter(this,calendarDay);
        listView.setAdapter(calendarDayAdapter);
        listView.setDividerHeight(0);
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
