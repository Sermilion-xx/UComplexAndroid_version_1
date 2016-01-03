package org.ucomplex.ucomplex.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMySubjectsTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.*;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutionException;



public class CourseActivity extends AppCompatActivity implements OnTaskCompleteListener {

    Stack<ArrayList<File>> stackFiles = new Stack<>();
    Toolbar toolbar;
    TabLayout tabLayout;
    ProgressDialog dialog;

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
        setContentView(R.layout.activity_course);
        dialog = ProgressDialog.show(CourseActivity.this, "",
                "Загрузка данных", true);
        dialog.show();

        Bundle extras = getIntent().getExtras();
        this.gcourse = extras.getInt("gcourse", -1);

        FetchMySubjectsTask fetchMySubjectsTask = new FetchMySubjectsTask(this, this);
        fetchMySubjectsTask.setmContext(this);
        fetchMySubjectsTask.setGcourse(this.gcourse);
        fetchMySubjectsTask.setupTask();

        FetchCalendarBeltTask fetchCalendarBeltTask = new FetchCalendarBeltTask(this, this);
        fetchCalendarBeltTask.setupTask(this.gcourse);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ArrayList files;
                if(courseMaterialsFragment.isMyFiles()){
                    if(stackFiles.size()>0){
                        files = stackFiles.pop();
                        courseMaterialsFragment.setFiles(files);
                        adapter.notifyDataSetChanged();
                        return true;
                    } else {
                        onBackPressed();
                        return true;
                    }
                }
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setmContext(this);

        Bundle cmBundel = new Bundle();
        cmBundel.putSerializable("courseData",coursedata);
        courseInfoFragment.setArguments(cmBundel);

        stackFiles.push(coursedata.getFiles());
        courseMaterialsFragment = new CourseMaterialsFragment();
        courseMaterialsFragment.setMyFiles(false);
        courseMaterialsFragment.setmContext(this);
        courseMaterialsFragment.setFiles(stackFiles.peek());

        calendarBeltFragment = new CalendarBeltFragment();
        calendarBeltFragment.setGcourse(this.gcourse);
        calendarBeltFragment.setFeedItems(this.feedItems);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(courseInfoFragment, "Дисциплина");
        adapter.addFragment(courseMaterialsFragment, "Материалы");
        adapter.addFragment(calendarBeltFragment, "Лента");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                if(task instanceof FetchTeacherFilesTask){
                    if(stackFiles.size()==0){
                        stackFiles.push((ArrayList<File>) task.get());
                        courseMaterialsFragment.setFiles(stackFiles.peek());
                    }else{
                        stackFiles.push((ArrayList<File>) task.get());
                        courseMaterialsFragment.setFiles(stackFiles.pop());
                    }
                    adapter.notifyDataSetChanged();
                }else if(task instanceof FetchMySubjectsTask){
                    try {
                        this.coursedata = (Course) task.get();
                        toolbar.setTitle(coursedata.getName());

                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else if(task instanceof FetchCalendarBeltTask){
                    try {
                        if(this.coursedata!=null) {
                            this.feedItems = (ArrayList<Quartet<Integer, String, String, Integer>>) task.get();
                            calendarBeltFragment = null;
                            courseMaterialsFragment = null;
                            courseInfoFragment = null;
                            adapter = null;
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if(coursedata!=null && feedItems!=null)
                    dialog.dismiss();




            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }



}