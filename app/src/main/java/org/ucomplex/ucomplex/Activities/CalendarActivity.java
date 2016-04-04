package org.ucomplex.ucomplex.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchAllStats;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Adaptors.CalendarInfoAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CalendarBeltFragment;
import org.ucomplex.ucomplex.Fragments.CalendarFragment;
import org.ucomplex.ucomplex.Fragments.CalendarStatisticsFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;
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
    CalendarStatisticsFragment statisticsFragment;
    User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user==null){
            user = Common.getUserDataFromPref(this);
        }
        int content_view;
        if (user.getType() == 4) {
            content_view = R.layout.activity_calendar;
        } else {
            content_view = R.layout.activity_calendar_teacher;
        }
        setContentView(content_view);
        viewPager = (ViewPager) findViewById(R.id.viewpager_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar2);
        toolbar.setTitle("Календарь");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        statisticsFragment = new CalendarStatisticsFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setContext(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        FetchAllStats fetchAllStats = new FetchAllStats(this, this);
        fetchAllStats.setupTask();
        //role related
        if (user.getType() == 4) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            if (calendarBeltFragment == null) {
                calendarBeltFragment = new CalendarBeltFragment();
                calendarBeltFragment.setmContext(this);
                if (this.feedItems == null) {
                    fetchCalendarBeltTask = new FetchCalendarBeltTask(this, this);
                    fetchCalendarBeltTask.setupTask();
                }
            }

            adapter.addFragment(calendarFragment, "Дисциплина");
            adapter.addFragment(calendarBeltFragment, "Лента");
            adapter.addFragment(statisticsFragment, "Статистика");
            viewPager.setAdapter(adapter);

            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setupWithViewPager(viewPager);

        } else if (user.getType() == 3) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_calendar, calendarFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
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
                        calendarBeltFragment.setmContext(CalendarActivity.this);
                    }
                    calendarBeltFragment.getCourseCalendarBeltAdapter().changeItems(feedItems);
                    calendarBeltFragment.checkLoadButton(feedItems);
                    calendarBeltFragment.setmContext(CalendarActivity.this);
                    if (feedItems.size() == 0) {
                        calendarBeltFragment.getListView().setBackgroundColor(ContextCompat.getColor(this, R.color.activity_background));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else if (task instanceof FetchAllStats) {
                try {
                    ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItems = ((FetchAllStats) task).get();
                    statisticsFragment.setStatisticItems(statisticItems);
                    System.out.println();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
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
