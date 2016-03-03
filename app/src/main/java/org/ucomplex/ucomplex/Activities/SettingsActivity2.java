package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.javatuples.Pair;
import org.ucomplex.ucomplex.Activities.Tasks.FetchProfileTask;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CalendarBeltFragment;
import org.ucomplex.ucomplex.Fragments.CourseFragment;
import org.ucomplex.ucomplex.Fragments.CourseInfoFragment;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Fragments.SettingsOneFragment;
import org.ucomplex.ucomplex.Fragments.SettingsTwoFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 29/02/16.
 */
public class SettingsActivity2 extends AppCompatActivity implements OnTaskCompleteListener {

    public static final int GET_FROM_GALLERY = 100;
    Toolbar toolbar;
    TabLayout tabLayout;

    SettingsOneFragment settingsOneFragment;
    SettingsTwoFragment settingsTwoFragment;

    ViewPagerAdapter adapter;
    ViewPager viewPager;

    public static ImageButton doneButton;

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Настройки");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        doneButton = (ImageButton) findViewById(R.id.settings_done);
        String filename = getIntent().getStringExtra("image");
        settingsOneFragment = new SettingsOneFragment();
        settingsOneFragment.setContext(this);
        settingsOneFragment.setFilename(filename);
        FetchProfileTask fetchProfileTask = new FetchProfileTask(this, this);
        fetchProfileTask.execute();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(settingsOneFragment, "Общая информация");
        adapter.addFragment(new Fragment(), "Личная информация");
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.settings_tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                settingsOneFragment.setProfileBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
//                profileBitmap = Common.getCroppedBitmap(profileBitmap, 604);
                settingsOneFragment.getPhotoImageView().setImageBitmap(settingsOneFragment.getProfileBitmap());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                settingsOneFragment.getProfileBitmap().compress(Bitmap.CompressFormat.JPEG, 60, bos);
                settingsOneFragment.setContentBody(new ByteArrayBody(bos.toByteArray(), "filename"));
                settingsOneFragment.getPhotoImageView().setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                SettingsOneFragment.PROFILE_IMAGE_CHANGED = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task instanceof FetchProfileTask) {
            try {
                Pair<String, String> privacy = ((FetchProfileTask) task).get();
                if (privacy != null) {
//                    settingsOneFragment.getClosedProfile().setChecked(false);
//                    settingsOneFragment.getHideProfile().setChecked(false);
//                    if(privacy.getValue0().equals("1")){
//                        settingsOneFragment.getClosedProfile().setChecked(true);
//                    }
//                    if(privacy.getValue1().equals("1"))
//                        settingsOneFragment.getHideProfile().setChecked(true);
                } else {
                    Toast.makeText(this, "Произошла ошибка (проверьте интернет соединение)", Toast.LENGTH_LONG)
                            .show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
