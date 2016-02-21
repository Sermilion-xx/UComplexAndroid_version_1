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

import org.ucomplex.ucomplex.Adaptors.CalendarInfoAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.CalendarFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.R;

public class CalendarActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnTaskCompleteListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    final Integer[] infoDrawables = {R.drawable.dot_1, R.drawable.dot_2, R.drawable.dot_3, R.drawable.dot_4, R.drawable.dot_5, R.drawable.dot_6, R.drawable.dot_7};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar2);
        toolbar.setTitle("Календарь");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        viewPager = (ViewPager) findViewById(R.id.viewpager_calendar);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setContext(this);
        adapter.addFragment(calendarFragment, "Дисциплина");
        adapter.addFragment(new Fragment(), "Материалы");
        adapter.addFragment(new Fragment(), "Лента");
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(viewPager.getCurrentItem()==0){
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
                final String [] items = new String[] {"Текущий день",
                        "Занятие/Событие", "Аттестация", "Экзамен",
                        "Индивидуальное занятие", "Нерабочий день", "Расписание"};

                ListAdapter adapter = new CalendarInfoAdapter(this, items, infoDrawables);

                new AlertDialog.Builder(this).setAdapter(adapter, null).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
