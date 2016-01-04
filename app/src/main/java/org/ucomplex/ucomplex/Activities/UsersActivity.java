package org.ucomplex.ucomplex.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Fragments.UsersFragment;
import org.ucomplex.ucomplex.R;


public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        toolbar.setTitle("Онлайн");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.users_viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    toolbar.setTitle("Онлайн");
                }else if(position==1){
                    toolbar.setTitle("Друзья");
                }else if(position==2){
                    toolbar.setTitle("Группа");
                }else if(position==3){
                    toolbar.setTitle("Преподаватели");
                }else if(position==4){
                    toolbar.setTitle("Заблокированные");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.users_tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);


        TextView tv0 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
        tv0.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        tv0.setTextSize(16);
        TextView tv1 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
        tv1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        tv1.setTextSize(18);
        TextView tv2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(2)).getChildAt(1));
        tv2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        TextView tv3 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(3)).getChildAt(1));
        tv3.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        TextView tv4 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(4)).getChildAt(1));
        tv4.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));

    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        UsersFragment onlineUsersFragment = new UsersFragment();
        onlineUsersFragment.setUsersType(0);
        adapter.addFragment(onlineUsersFragment, "\uF007");

        UsersFragment friendsUsersFragment = new UsersFragment();
        friendsUsersFragment.setUsersType(1);
        adapter.addFragment(friendsUsersFragment, "\uF234");

        UsersFragment groupsUsersFragment = new UsersFragment();
        groupsUsersFragment.setUsersType(2);
        adapter.addFragment(groupsUsersFragment, "\uF0C0");

        UsersFragment teachersUsersFragment = new UsersFragment();
        teachersUsersFragment.setUsersType(3);
        adapter.addFragment(teachersUsersFragment, "\uF21B");

        UsersFragment blacklistUsersFragment = new UsersFragment();
        blacklistUsersFragment.setUsersType(4);
        adapter.addFragment(blacklistUsersFragment, "\uF235");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
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
