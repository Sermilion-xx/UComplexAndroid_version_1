package org.ucomplex.ucomplex.Activities;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMySubjectsTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.CalendarBeltFragment;
import org.ucomplex.ucomplex.Fragments.CourseFragment;
import org.ucomplex.ucomplex.Fragments.CourseInfoFragment;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class CourseActivity extends AppCompatActivity implements OnTaskCompleteListener, CourseMaterialsFragment.OnHeadlineSelectedListener {

    Toolbar toolbar;
    TabLayout tabLayout;

    private int gcourse;
    String courseName;
    private Course coursedata;
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;
    ArrayList<String> titles = new ArrayList<>();


    CourseMaterialsFragment courseMaterialsFragment;
    CourseInfoFragment courseInfoFragment;
    CourseFragment courseFragment;
    CalendarBeltFragment calendarBeltFragment;
    ViewPagerAdapter adapter;
    ViewPager mViewPager;
    boolean loaded;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.gcourse = extras.getInt("gcourse", -1);
        this.courseName = extras.getString("courseName");
        titles.add(this.courseName);
        setContentView(R.layout.activity_course);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    if(courseMaterialsFragment!=null){
                        if (courseMaterialsFragment.getAdapter().getLevel() > 0) {
                            toolbar.setTitle(titles.get(courseMaterialsFragment.getAdapter().getLevel()));
                        }
                    }
                } else {
                    toolbar.setTitle(courseName);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        tabLayout.setupWithViewPager(mViewPager);


        FetchMySubjectsTask fetchMySubjectsTask = new FetchMySubjectsTask(this, this);
        fetchMySubjectsTask.setmContext(this);
        fetchMySubjectsTask.setGcourse(this.gcourse);
        fetchMySubjectsTask.setupTask();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("courseName"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FetchCalendarBeltTask fetchCalendarBeltTask = new FetchCalendarBeltTask(this, this);
        fetchCalendarBeltTask.setupTask(this.gcourse);
    }


    @Override
    public void onBackPressed() {
        if (courseMaterialsFragment == null) {
            super.onBackPressed();
        } else if (courseMaterialsFragment.getAdapter().getLevel() == 0) {
            toolbar.setTitle(this.courseName);
            super.onBackPressed();
        } else {
            toolbar.setTitle(titles.get(courseMaterialsFragment.getAdapter().getLevel() - 1));
            if (!courseMaterialsFragment.getAdapter().isMyFiles()) {
                if (courseMaterialsFragment.getAdapter().getLevel() > 0) {
                    courseMaterialsFragment.getAdapter().levelDown();
                    courseMaterialsFragment.getmItems().clear();
                    ArrayList<File> newFiles = new ArrayList<>(courseMaterialsFragment.getAdapter().getStackFiles().get(courseMaterialsFragment.getAdapter().getLevel()));

                    if (!courseMaterialsFragment.getAdapter().isMyFiles()) {
                        courseMaterialsFragment.getAdapter().getStackFiles().remove(courseMaterialsFragment.getAdapter().getStackFiles().size() - 1);
                    }
                    courseMaterialsFragment.getmItems().addAll(newFiles);
                    courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (courseMaterialsFragment != null) {
                    if (courseMaterialsFragment.getAdapter().getLevel() > 0) {
                        courseMaterialsFragment.getAdapter().levelDown();
                        if (courseMaterialsFragment.getAdapter().getLevel() == 0) {
                            toolbar.setTitle(this.courseName);
                        }
                        courseMaterialsFragment.getmItems().clear();
                        ArrayList<File> newFiles = new ArrayList<>(courseMaterialsFragment.getAdapter().getStackFiles().get(courseMaterialsFragment.getAdapter().getLevel()));

                        if (!courseMaterialsFragment.getAdapter().isMyFiles()) {
                            courseMaterialsFragment.getAdapter().getStackFiles().remove(courseMaterialsFragment.getAdapter().getStackFiles().size() - 1);
                        }
                        courseMaterialsFragment.getmItems().addAll(newFiles);
                        courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                        return true;
                    } else {
                        onBackPressed();
                        return true;
                    }
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

        courseFragment = new CourseFragment();
        courseFragment.setmContext(this);
        courseFragment.setArguments(cmBundel);

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
        adapter.addFragment(courseFragment, "Дисциплина");
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
                        if (courseMaterialsFragment == null) {
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
                        if (this.coursedata != null) {
                            setupViewPager(mViewPager);
                            tabLayout.setupWithViewPager(mViewPager);
                            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                            loaded = true;

                        }
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
                            if (this.feedItems != null && this.feedItems.size() > 0) {
                                calendarBeltFragment.setFeedItems(feedItems);
                                calendarBeltFragment.initAdapter(CourseActivity.this);
                                calendarBeltFragment.getCourseCalendarBeltAdapter().notifyDataSetChanged();
                            }

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

    @Override
    public void onFolderSelect(String title) {
        toolbar.setTitle(title);
        this.titles.add(title);
    }
}