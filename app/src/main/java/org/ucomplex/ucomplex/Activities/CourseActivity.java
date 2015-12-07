package org.ucomplex.ucomplex.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMySubjectsTask;
import org.ucomplex.ucomplex.Fragments.*;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseActivity extends AppCompatActivity {

    private int gcourse;
    private String jsonData;
    private Course coursedata;
    private int type;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle extras = getIntent().getExtras();
        this.gcourse = extras.getInt("gcourse", -1);
        this.type = extras.getInt("type", -1);

        FetchMySubjectsTask fetchMySubjectsTask = new FetchMySubjectsTask();
        fetchMySubjectsTask.setmContext(this);
        fetchMySubjectsTask.setGcourse(this.gcourse);
        try {
            this.coursedata = fetchMySubjectsTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setmContext(this);
        courseInfoFragment.setType(this.type);
        courseInfoFragment.setBitmap(this.bitmap);

        Bundle cmBundel = new Bundle();
        cmBundel.putSerializable("courseData",coursedata);
        courseInfoFragment.setArguments(cmBundel);

        CourseMaterialsFragment courseMaterialsFragment = new CourseMaterialsFragment();
        courseMaterialsFragment.setFiles(coursedata.getFiles());

        CalendarBeltFragment calendarBeltFragment = new CalendarBeltFragment();
        calendarBeltFragment.setGcourse(this.gcourse);

        adapter.addFragment(courseInfoFragment, "Дисциплина");
        adapter.addFragment(courseMaterialsFragment, "Материалы");
        adapter.addFragment(calendarBeltFragment, "Лента");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}