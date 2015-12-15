package org.ucomplex.ucomplex.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMySubjectsTask;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.*;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class CourseActivity extends AppCompatActivity {

    private int gcourse;
    private String jsonData;
    private Course coursedata;
    private Bitmap bitmap;
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;

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

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setmContext(this);
        courseInfoFragment.setBitmap(this.bitmap);

        Bundle cmBundel = new Bundle();
        cmBundel.putSerializable("courseData",coursedata);
        courseInfoFragment.setArguments(cmBundel);

        CourseMaterialsFragment courseMaterialsFragment = new CourseMaterialsFragment();
        courseMaterialsFragment.setFiles(coursedata.getFiles());

        CalendarBeltFragment calendarBeltFragment = new CalendarBeltFragment();
        calendarBeltFragment.setGcourse(this.gcourse);
        calendarBeltFragment.setFeedItems(this.feedItems);

        adapter.addFragment(courseInfoFragment, "Дисциплина");
        adapter.addFragment(courseMaterialsFragment, "Материалы");
        adapter.addFragment(calendarBeltFragment, "Лента");
        viewPager.setAdapter(adapter);
    }




}