package org.ucomplex.ucomplex.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchAllStats;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Adaptors.CalendarInfoAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.CalendarBeltFragment;
import org.ucomplex.ucomplex.Fragments.CalendarFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnTaskCompleteListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    final Integer[] infoDrawables = {R.drawable.dot_1, R.drawable.dot_2, R.drawable.dot_3, R.drawable.dot_4, R.drawable.dot_5, R.drawable.dot_6, R.drawable.dot_7};
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;
    CalendarBeltFragment calendarBeltFragment;
    FetchCalendarBeltTask fetchCalendarBeltTask;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar2);
        toolbar.setTitle("Календарь");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        viewPager = (ViewPager) findViewById(R.id.viewpager_calendar);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (calendarBeltFragment == null) {
            calendarBeltFragment = new CalendarBeltFragment();
            if (this.feedItems == null) {
                fetchCalendarBeltTask = new FetchCalendarBeltTask(this, this);
                fetchCalendarBeltTask.setupTask();
            } else {
                calendarBeltFragment.setFeedItems(this.feedItems);
            }
        }

        FetchAllStats fetchAllStats = new FetchAllStats(this, this);
        fetchAllStats.setupTask();


        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setContext(this);
        adapter.addFragment(calendarFragment, "Дисциплина");
        adapter.addFragment(calendarBeltFragment, "Лента");
        adapter.addFragment(new Fragment(), "Статистика");
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG).show();
        } else {
            if (task instanceof FetchCalendarBeltTask) {
                try {
                    this.feedItems = (ArrayList<Quartet<Integer, String, String, Integer>>) task.get();
                    if (calendarBeltFragment == null) {
                        calendarBeltFragment = new CalendarBeltFragment();
                    }
                    calendarBeltFragment.getCourseCalendarBeltAdapter().changeItems(feedItems);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }else if (task instanceof FetchAllStats) {
                try {
                    ArrayList<Quartet<String, String, String, String>> statisticItems = ((FetchAllStats) task).get();
                    System.out.println();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewPager.getCurrentItem() == 0) {
            getMenuInflater().inflate(R.menu.menu_calendar, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_calendar_info:
                final String[] items = new String[]{"Текущий день",
                        "Занятие/Событие", "Аттестация", "Экзамен",
                        "Индивидуальное занятие", "Нерабочий день", "Расписание"};

                ListAdapter adapter = new CalendarInfoAdapter(this, items, infoDrawables);

                new AlertDialog.Builder(this).setAdapter(adapter, null).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
