package org.ucomplex.ucomplex.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.UsersFragment;
import org.ucomplex.ucomplex.R;


public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.users_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.users_tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        UsersFragment onlineUsersFragment = new UsersFragment();
        onlineUsersFragment.setUsersType(0);
        adapter.addFragment(onlineUsersFragment, "Онлайн");

        UsersFragment friendsUsersFragment = new UsersFragment();
        friendsUsersFragment.setUsersType(1);
        adapter.addFragment(friendsUsersFragment, "Друзья");

        UsersFragment groupsUsersFragment = new UsersFragment();
        groupsUsersFragment.setUsersType(2);
        adapter.addFragment(groupsUsersFragment, "Группы");

        UsersFragment teachersUsersFragment = new UsersFragment();
        teachersUsersFragment.setUsersType(3);
        adapter.addFragment(teachersUsersFragment, "Преподаватели");

        UsersFragment blacklistUsersFragment = new UsersFragment();
        blacklistUsersFragment.setUsersType(4);
        adapter.addFragment(blacklistUsersFragment, "Черный список");

        viewPager.setAdapter(adapter);
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
