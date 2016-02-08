package org.ucomplex.ucomplex.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMySubjectsTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.*;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class CourseActivity extends AppCompatActivity implements OnTaskCompleteListener {

    Toolbar toolbar;
    TabLayout tabLayout;

    boolean first = true;
    private int gcourse;
    private Course coursedata;
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;

    CourseMaterialsFragment courseMaterialsFragment;
    CourseInfoFragment courseInfoFragment;
    CalendarBeltFragment calendarBeltFragment;
    ViewPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.gcourse = extras.getInt("gcourse", -1);
        FetchMySubjectsTask fetchMySubjectsTask = new FetchMySubjectsTask(this, this);
        fetchMySubjectsTask.setmContext(this);
        fetchMySubjectsTask.setGcourse(this.gcourse);
        fetchMySubjectsTask.setupTask();

        setContentView(R.layout.activity_course);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CourseInfoFragment(), "Дисциплина");
        adapter.addFragment(new Fragment(), "Материалы");
        adapter.addFragment(new Fragment(), "Лента");
        viewPager.setAdapter(adapter);

        FetchCalendarBeltTask fetchCalendarBeltTask = new FetchCalendarBeltTask(this, this);
        fetchCalendarBeltTask.setupTask(this.gcourse);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (courseMaterialsFragment.getAdapter().getLevel() > 0) {
                    courseMaterialsFragment.getAdapter().levelDown();
                    courseMaterialsFragment.getmItems().clear();
                    ArrayList<File> newFiles = new ArrayList<>(courseMaterialsFragment.getAdapter().getStackFiles().get(courseMaterialsFragment.getAdapter().getLevel()));

                    if(!courseMaterialsFragment.getAdapter().isMyFiles()){
                        courseMaterialsFragment.getAdapter().getStackFiles().remove(courseMaterialsFragment.getAdapter().getStackFiles().size()-1);
                    }
                    courseMaterialsFragment.getmItems().addAll(newFiles);
                    courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                    return true;
                } else {
                    onBackPressed();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setmContext(this);

        Bundle cmBundel = new Bundle();
        cmBundel.putSerializable("courseData", coursedata);
        courseInfoFragment.setArguments(cmBundel);

        if (courseMaterialsFragment == null) {
            courseMaterialsFragment = new CourseMaterialsFragment();
        }
        courseMaterialsFragment.setMyFiles(false);
        courseMaterialsFragment.setmContext(this);
        CourseMaterialsAdapter courseMaterialsAdapter = new CourseMaterialsAdapter(this, coursedata.getFiles(), false, courseMaterialsFragment);
        courseMaterialsFragment.setFiles(coursedata.getFiles());
        courseMaterialsAdapter.addStack(coursedata.getFiles());
        courseMaterialsAdapter.setLevel(0);
        courseMaterialsFragment.setAdapter(courseMaterialsAdapter);


        if (calendarBeltFragment == null) {
            calendarBeltFragment = new CalendarBeltFragment();
            calendarBeltFragment.setFeedItems(this.feedItems);
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(courseInfoFragment, "Дисциплина");
        adapter.addFragment(courseMaterialsFragment, "Материалы");
        adapter.addFragment(calendarBeltFragment, "Лента");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG).show();
        } else {
            try {
                if (task instanceof FetchTeacherFilesTask) {
                    ArrayList<File> files = (ArrayList<File>) task.get();
                    if (files.size() > 0) {
                        if(courseMaterialsFragment==null){
                            courseMaterialsFragment = new CourseMaterialsFragment();
                            courseMaterialsFragment.setMyFiles(false);
                            courseMaterialsFragment.setmContext(this);
                        }
                        courseMaterialsFragment.getAdapter().addStack(files);
                        courseMaterialsFragment.getmItems().clear();
                        courseMaterialsFragment.getmItems().addAll(files);
                        courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                    }
                } else if (task instanceof FetchMySubjectsTask) {
                    try {
                        this.coursedata = (Course) task.get();
                        toolbar.setTitle(coursedata.getName());
                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else if (task instanceof FetchCalendarBeltTask) {
                    try {
                        if (this.coursedata != null) {
                            this.feedItems = (ArrayList<Quartet<Integer, String, String, Integer>>) task.get();
                            if (calendarBeltFragment == null) {
                                calendarBeltFragment = new CalendarBeltFragment();
                            }
                            calendarBeltFragment.setFeedItems(feedItems);
                            calendarBeltFragment.initAdapter(CourseActivity.this);
                            calendarBeltFragment.getCourseCalendarBeltAdapter().notifyDataSetChanged();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}