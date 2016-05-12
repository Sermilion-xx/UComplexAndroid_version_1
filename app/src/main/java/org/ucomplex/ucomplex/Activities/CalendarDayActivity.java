package org.ucomplex.ucomplex.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CalendarBeltFragment;
import org.ucomplex.ucomplex.Fragments.CalendarTimetableFragment;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

public class CalendarDayActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;

    CalendarTimetableFragment calendarTimetableFragment;
    CalendarBeltFragment calendarBeltFragment;

    ViewPagerAdapter adapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle((String) getIntent().getExtras().get("date"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        ArrayList calendarDay = (ArrayList) getIntent().getExtras().get("calendarDay");
        ArrayList calendarBeltDay = (ArrayList) getIntent().getExtras().get("calendarBeltDay");
        calendarTimetableFragment = new CalendarTimetableFragment();
        calendarTimetableFragment.setCalendarDay(calendarDay);
        if(Common.USER_TYPE == 3){
            mViewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_calendar_day, calendarTimetableFragment);
            fragmentTransaction.commit();
        }else{
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            calendarBeltFragment = new CalendarBeltFragment();
            calendarBeltFragment.setFeedItems(calendarBeltDay);
            adapter.addFragment(calendarTimetableFragment, "Расписание");
            adapter.addFragment(calendarBeltFragment, "Успеваемость");
            mViewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(mViewPager);
        }

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
