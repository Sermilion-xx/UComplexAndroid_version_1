package org.ucomplex.ucomplex.Activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMySubjectsTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.*;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class CourseActivity extends AppCompatActivity implements OnTaskCompleteListener {

    private int gcourse;
    private String jsonData;
    private Course coursedata;
    private Bitmap bitmap;
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;
    ArrayList<Fragment> fragmentList;

    CourseMaterialsFragment courseMaterialsFragment;
    CourseInfoFragment courseInfoFragment;
    CalendarBeltFragment calendarBeltFragment;
    ViewPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle extras = getIntent().getExtras();
        this.gcourse = extras.getInt("gcourse", -1);


        FetchMySubjectsTask fetchMySubjectsTask = new FetchMySubjectsTask();
        fetchMySubjectsTask.setmContext(this);
        fetchMySubjectsTask.setGcourse(this.gcourse);
        try {
            this.coursedata = fetchMySubjectsTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        FetchCalendarBeltTask fetchCalendarBeltTask = new FetchCalendarBeltTask();
        fetchCalendarBeltTask.setmContext(this);
        try {
            this.feedItems = fetchCalendarBeltTask.execute(this.gcourse).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(coursedata.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
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

    private void setupViewPager(ViewPager viewPager) {


        courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setmContext(this);
        courseInfoFragment.setBitmap(this.bitmap);

        Bundle cmBundel = new Bundle();
        cmBundel.putSerializable("courseData",coursedata);
        courseInfoFragment.setArguments(cmBundel);

        courseMaterialsFragment = new CourseMaterialsFragment();
        courseMaterialsFragment.setmContext(this);
        courseMaterialsFragment.setFiles(coursedata.getFiles());

        calendarBeltFragment = new CalendarBeltFragment();
        calendarBeltFragment.setGcourse(this.gcourse);
        calendarBeltFragment.setFeedItems(this.feedItems);

        fragmentList = new ArrayList<>();
        fragmentList.add(courseInfoFragment);
        fragmentList.add(courseMaterialsFragment);
        fragmentList.add(calendarBeltFragment);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(courseInfoFragment, "Дисциплина");
        adapter.addFragment(courseMaterialsFragment, "Материалы");
        adapter.addFragment(calendarBeltFragment, "Лента");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            // Report about cancel
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            ArrayList<File> file = new ArrayList();
            try {
                file = (ArrayList<File>) task.get();
                courseMaterialsFragment.setFiles(file);
                adapter.notifyDataSetChanged();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }



}