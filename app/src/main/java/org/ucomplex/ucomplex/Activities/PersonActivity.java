package org.ucomplex.ucomplex.Activities;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import org.ucomplex.ucomplex.Fragments.CourseInfoFragment;
import org.ucomplex.ucomplex.R;

public class PersonActivity extends AppCompatActivity {

    int userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Профиль");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            userId = Integer.parseInt(extra.getString("userId"));
        }

        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setPerson(userId);
        courseInfoFragment.setmContext(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_person, courseInfoFragment);
        fragmentTransaction.commit();

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
