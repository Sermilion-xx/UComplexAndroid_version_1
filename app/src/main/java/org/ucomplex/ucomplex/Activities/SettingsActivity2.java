package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.javatuples.Pair;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.Tasks.FetchProfileTask;
import org.ucomplex.ucomplex.Activities.Tasks.SettingsTask;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.SettingsOneFragment;
import org.ucomplex.ucomplex.Fragments.SettingsTwoFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.io.ByteArrayOutputStream;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
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
        if (Common.ROLE == 3) {
            settingsTwoFragment = new SettingsTwoFragment();
            settingsTwoFragment.setmContext(this);
        }
        FetchProfileTask fetchProfileTask = new FetchProfileTask(this, this);
        fetchProfileTask.execute();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkConnected(SettingsActivity2.this)) {
                    //----------------------Settings One--------------------------------------------
                    if (SettingsOneFragment.PROFILE_IMAGE_CHANGED) {
                        UploadPhotoTask uploadPhotoTask = new UploadPhotoTask(SettingsActivity2.this, SettingsActivity2.this);
                        uploadPhotoTask.setupTask(settingsOneFragment.getContentBody());
                        SettingsOneFragment.PROFILE_IMAGE_CHANGED = false;
                    }
                    if (SettingsOneFragment.CURRENT_PASSWORD_CHANGE || SettingsOneFragment.NEW_PASSWORD_CHANGE || SettingsOneFragment.NEW_PASSWORD_AGAIN_CHANGE) {
                        settingsOneFragment.resetPassword(settingsOneFragment.currentPasswordTextView,
                                settingsOneFragment.newPasswordTextView,
                                settingsOneFragment.newPasswordAgainTextView, settingsOneFragment.user);
                    } else if (SettingsOneFragment.CURRENT_PASSWORD_CHANGE || SettingsOneFragment.NEW_PASSWORD_CHANGE || SettingsOneFragment.NEW_PASSWORD_AGAIN_CHANGE) {
                        Toast.makeText(SettingsActivity2.this, "Заполните все поля!", Toast.LENGTH_LONG).show();
                    }
                    if (SettingsOneFragment.NEW_EMAIL_CHANGE || SettingsOneFragment.NEW_EMAIL_PASSWORD_CHANGE) {
                        settingsOneFragment.changeEmail(settingsOneFragment.passwordEmalTextView, settingsOneFragment.newEmalTextView);
                    } else if (SettingsOneFragment.NEW_EMAIL_CHANGE || SettingsOneFragment.NEW_EMAIL_PASSWORD_CHANGE) {
                        Toast.makeText(SettingsActivity2.this, "Заполните все поля!", Toast.LENGTH_LONG).show();
                    }
                    if (SettingsOneFragment.NEW_PHONE_CHANGED || SettingsOneFragment.NEW_PHONE_PASSWORD_CHANGE) {
                        settingsOneFragment.changePhoneNumber(settingsOneFragment.newPhoneTextView, settingsOneFragment.oldPasswordPhoneTextView);
                    } else if (SettingsOneFragment.NEW_PHONE_CHANGED || SettingsOneFragment.NEW_PHONE_PASSWORD_CHANGE) {
                        Toast.makeText(SettingsActivity2.this, "Заполните все поля!", Toast.LENGTH_LONG).show();
                    }
                    //----------------------Settings Two--------------------------------------------
                    if (SettingsTwoFragment.STUDY_RANK_CHANGED || SettingsTwoFragment.BIO_CHANGED || SettingsTwoFragment.STUDY_DEGREE_CHANGED
                            || SettingsTwoFragment.DISCIPLINES_CHANGED || SettingsTwoFragment.STUDY_DEGREE_CHANGED_2
                            || SettingsTwoFragment.UPQUALIFICATIONS_CHANGED) {
                        settingsTwoFragment.saveSettingsTwo();
                    }

                } else {
                    Toast.makeText(SettingsActivity2.this, "Проверьте интернет соединение.", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(settingsOneFragment, "Общая информация");
        if (Common.ROLE == 3) {
            adapter.addFragment(settingsTwoFragment, "Личные данные");
        }
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.settings_tabs);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
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
                Pair<Pair<String, String>, JSONObject> profileSettings = ((FetchProfileTask) task).get();
                Pair<String, String> privacy = profileSettings.getValue0();
                if (Common.ROLE == 3) {
                    settingsTwoFragment.setCustomInfoSettings(profileSettings.getValue1());
                }
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
        } else if (task instanceof SettingsTask) {
            if (task.isCancelled()) {
                Toast.makeText(SettingsActivity2.this, "Операция отменена", Toast.LENGTH_LONG)
                        .show();
            } else {
                try {
                    User user = Common.getUserDataFromPref(SettingsActivity2.this);
                    if (SettingsOneFragment.CURRENT_PASSWORD_CHANGE || SettingsOneFragment.NEW_PASSWORD_CHANGE || SettingsOneFragment.NEW_PASSWORD_AGAIN_CHANGE) {
                        if (!settingsOneFragment.newPasswordTextView.getText().toString().equals("")) {
                            user.setPass(settingsOneFragment.newPasswordTextView.getText().toString());
                            Common.setUserDataToPref(SettingsActivity2.this, user);
                            settingsOneFragment.user.setPass(settingsOneFragment.newPasswordTextView.getText().toString());
                        }
                        SettingsOneFragment.CURRENT_PASSWORD_CHANGE = false;
                        SettingsOneFragment.NEW_PASSWORD_CHANGE = false;
                        SettingsOneFragment.NEW_PASSWORD_AGAIN_CHANGE = false;
                        settingsOneFragment.newPasswordTextView.setText("");
                        settingsOneFragment.newPasswordAgainTextView.setText("");
                        settingsOneFragment.currentPasswordTextView.setText("");

                    }
                    if (task.get() != null) {
                        if (task.get().equals("success")) {
                            if ((int) o[0] == 3) {
                                //phone
                                if (!settingsOneFragment.newPhoneTextView.getText().toString().equals("")) {
                                    user.setPhone(settingsOneFragment.newPhoneTextView.getText().toString());
                                    Common.setUserDataToPref(SettingsActivity2.this, user);
                                }
                                String phone = settingsOneFragment.formatPhoneNumber(user.getPhone());
                                settingsOneFragment.oldPhoneTextView.setText(phone);
                                settingsOneFragment.oldPasswordPhoneTextView.setText("");
                                settingsOneFragment.newPhoneTextView.setText("");
                                SettingsOneFragment.NEW_PHONE_PASSWORD_CHANGE = false;
                                SettingsOneFragment.NEW_PHONE_CHANGED = false;
                            } else if ((int) o[0] == 2) {
                                //email
                                if (!settingsOneFragment.newEmalTextView.getText().toString().equals("")) {
                                    user.setEmail(settingsOneFragment.newEmalTextView.getText().toString());
                                    Common.setUserDataToPref(SettingsActivity2.this, user);
                                }
                                settingsOneFragment.currentEmalTextView.setText(user.getEmail());
                                settingsOneFragment.passwordEmalTextView.setText("");
                                settingsOneFragment.newEmalTextView.setText("");
                                SettingsOneFragment.NEW_EMAIL_PASSWORD_CHANGE = false;
                                SettingsOneFragment.NEW_EMAIL_CHANGE = false;
                            } else if ((int) o[0] == 4) {
                                SettingsOneFragment.PRIVACY_CHANGED = false;
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingsActivity2.this).edit();
                                editor.putString("closedProfile", settingsOneFragment.closedPrifileStr);
                                editor.putString("searchableProfile", settingsOneFragment.searchablePrifileStr);
                                editor.apply();
                            }
                            Toast.makeText(SettingsActivity2.this, "Настройки сохранены", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SettingsActivity2.this, "Произошла ошибка", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SettingsActivity2.this, "Произошла ошибка (проверьте интернет соединение)", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
