package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.javatuples.Pair;

import org.ucomplex.ucomplex.Activities.Tasks.FetchProfileTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Activities.Tasks.SettingsTask;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class SettingsActivity extends AppCompatActivity implements OnTaskCompleteListener {

    public static final int GET_FROM_GALLERY = 100;
    private Bitmap profileBitmap;
    ImageView photoImageView;
    SettingsActivity context = this;
    ByteArrayBody contentBody;
    boolean chose = false;
    User user;

    TextView oldPhoneTextView;
    TextView currentEmalTextView;
    String closedPrifileStr;
    String searchablePrifileStr;

    CheckBox closedProfile;
    CheckBox hideProfile;
    Button privacyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Настройки");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        user = Common.getUserDataFromPref(this);
        FetchProfileTask fetchProfileTask = new FetchProfileTask(this,this);
        fetchProfileTask.execute();
        //Photo settings
        photoImageView = (ImageView) findViewById(R.id.settings_photo);

        Bitmap bmp = null;

        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(bmp==null){
            final int colorsCount = 16;
            final int number = (user.getPerson() <= colorsCount) ? user.getPerson() : user.getPerson() % colorsCount;
            char  firstLetter = user.getName().split(" ")[1].charAt(0);
            TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .width(604)
                    .height(604)
                    .endConfig()
                    .buildRect(String.valueOf(firstLetter), Common.getColor(number));
            photoImageView.setImageDrawable(drawable);
        }else {
            photoImageView.setImageBitmap(bmp);
        }
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GET_FROM_GALLERY);
                chose = true;
            }
        });

        Button buttonChangePhoto = (Button) findViewById(R.id.settings_photo_change_button);
        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chose){
                    UploadPhotoTask uploadPhotoTask = new UploadPhotoTask(context, context);
                    uploadPhotoTask.setupTask(contentBody);
                    chose = false;
                }
            }
        });

            //Password settings
            final TextView currentPasswordTextView = (TextView) findViewById(R.id.settings_password_current);
            final TextView newPasswordTextView = (TextView) findViewById(R.id.settings_password_new);
            final TextView newPasswordAgainTextView = (TextView) findViewById(R.id.settings_password_again);

            Button changePassButton = (Button) findViewById(R.id.settings_password_reset_button);
            changePassButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    resetPassword(currentPasswordTextView, newPasswordTextView, newPasswordAgainTextView, user);
                }
            });

            //Phone settings
            final TextView newPhoneTextView = (TextView) findViewById(R.id.settings_phone_new);
            final TextView oldPasswordPhoneTextView = (TextView) findViewById(R.id.settings_phone_password_current_phone);
            oldPhoneTextView = (TextView) findViewById(R.id.settings_phone_number);
            String phone = formatPhoneNumber(user.getPhone());
            oldPhoneTextView.setText(phone);
            Button changePhoneButton = (Button) findViewById(R.id.settings_phone_reset_button);
            changePhoneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   changePhoneNumber(newPhoneTextView,oldPasswordPhoneTextView);
                }
            });

            //Email setting
            currentEmalTextView = (TextView) findViewById(R.id.settings_email_current);
            currentEmalTextView.setText(user.getEmail());
            final TextView passwordEmalTextView = (TextView) findViewById(R.id.settings_email_password);
            final TextView newEmalTextView = (TextView) findViewById(R.id.settings_email_new);
            Button chamgeEmailButton = (Button) findViewById(R.id.setting_email_button);
            chamgeEmailButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    changeEmail(passwordEmalTextView, newEmalTextView);
                }
            });

            //Privacy settings
            privacyButton = (Button) findViewById(R.id.settings_privacy_button);
            privacyButton.setFocusable(false);
            closedProfile = (CheckBox) findViewById(R.id.settings_privacy_closed_profile);
            closedProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                          @Override
                          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                              privacyButton.setFocusable(true);
                          }
            }
            );
            hideProfile = (CheckBox) findViewById(R.id.settings_privacy_hide_profile);
            hideProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        privacyButton.setFocusable(true);
                                                    }
                                                }
        );
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            closedPrifileStr = pref.getString("closedProfile", "0");
            searchablePrifileStr = pref.getString("searchableProfile", "0");
            if(closedPrifileStr.equals("1"))
                closedProfile.setChecked(true);
            if(searchablePrifileStr.equals("1"))
                hideProfile.setChecked(true);
            privacyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    changePrivacy(closedProfile, hideProfile);
                }
            });
    }

    private void changePrivacy(CheckBox closedPrifile, CheckBox hideProfile) {
        if(closedPrifile.isChecked())
            closedPrifileStr = "1";
        else
            closedPrifileStr = "0";
        if(hideProfile.isChecked())
            searchablePrifileStr = "1";
        else
            searchablePrifileStr = "0";

        Pair<String, String> httpParams1 = new Pair<>("closed", closedPrifileStr);
        Pair<String, String> httpParams2 = new Pair<>("searchable", searchablePrifileStr);
        Pair<String, String> httpParams3 = new Pair<>("privacy", "");
        SettingsTask settingsTask = new SettingsTask(this, this);
        settingsTask.setContext(context);
        settingsTask.execute(httpParams1,httpParams2,httpParams3);
    }

    private void changeEmail(TextView passwordEmalTextView, TextView newEmalTextView) {
        final String newEmal = newEmalTextView.getText().toString();
        final String currentPassword = passwordEmalTextView.getText().toString();

        passwordEmalTextView.setError(null);
        newEmalTextView.setError(null);

        boolean cancel = false;
        View focusView = null;


        if ((TextUtils.isEmpty(newEmal) || !isEmailValid(newEmal))) {
            newEmalTextView.setError("Введенный адрес не корректный");
            focusView = newEmalTextView;
            cancel = true;
        }

        if ((TextUtils.isEmpty(currentPassword) || !isPasswordValid(currentPassword))) {
            passwordEmalTextView.setError("Слишком короткий пароль");
            focusView = passwordEmalTextView;
            cancel = true;
        }

        if(!currentPassword.equals(user.getPass())){
            passwordEmalTextView.setError("Неверный пароль");
            focusView = passwordEmalTextView;
            cancel = true;
        }

        if(!cancel){
            Pair<String, String> httpParams1 = new Pair<>("email", newEmal);
            Pair<String, String> httpParams2 = new Pair<>("currpass", currentPassword);
            Pair<String, String> httpParams3 = new Pair<>("email", newEmal);
            SettingsTask settingsTask = new SettingsTask(this, this);
            settingsTask.setContext(context);
            settingsTask.execute(httpParams1,httpParams2,httpParams3);
        }else{
            focusView.requestFocus();
        }

    }

    private boolean isEmailValid(String email) {
        if(email.contains("@")){
           String[] splitEmail = email.split("@");
           if(splitEmail.length == 2){
               if(splitEmail[1].contains(".")){
                   return true;
               }
           }
        }
        return false;
    }

    private String formatPhoneNumber(String phoneNumber){
        return phoneNumber.replace(" ","").replaceAll("\\b(\\d{4})\\d+(\\d{2})", "$1*****$2");
    }


    private void changePhoneNumber(TextView newPhoneTextView, TextView oldPasswordPhoneTextView){
        final String newPhoneNumber = newPhoneTextView.getText().toString();
        final String currentPassword = oldPasswordPhoneTextView.getText().toString();
        newPhoneTextView.setError(null);
        oldPasswordPhoneTextView.setError(null);
        boolean cancel = false;
        View focusView = null;

        if ((TextUtils.isEmpty(newPhoneNumber) || !isPhoneNumberValid(newPhoneNumber))) {
            newPhoneTextView.setError("Слишком короткий номер телефона");
            focusView = newPhoneTextView;
            cancel = true;
        }

        if(newPhoneNumber.length()>0) {
            if (newPhoneNumber.charAt(0) != '+') {
                newPhoneTextView.setError("Неверный формат номера");
                focusView = newPhoneTextView;
                cancel = true;
            }
        }

        if ((TextUtils.isEmpty(currentPassword) || !isPasswordValid(currentPassword))) {
            oldPasswordPhoneTextView.setError("Слишком короткий пароль");
            focusView = oldPasswordPhoneTextView;
            cancel = true;
        }

        if(!currentPassword.equals(user.getPass())){
            oldPasswordPhoneTextView.setError("Неверный пароль");
            focusView = oldPasswordPhoneTextView;
            cancel = true;
        }

        if(!cancel){
            Pair<String, String> httpParams1 = new Pair<>("phone", newPhoneNumber);
            Pair<String, String> httpParams2 = new Pair<>("currpass", currentPassword);
            Pair<String, String> httpParams3 = new Pair<>("phone", newPhoneNumber);
            SettingsTask settingsTask = new SettingsTask(this, this);
            settingsTask.setContext(context);
            settingsTask.execute(httpParams1,httpParams2,httpParams3);
        }else{
            focusView.requestFocus();
        }

    }

    private void resetPassword(TextView currentPasswordTextView, TextView newPasswordTextView, TextView newPasswordAgainTextView,
    User user){
        final String oldPass = currentPasswordTextView.getText().toString();
        final String newPass = newPasswordTextView.getText().toString();
        final String newPassAgain = newPasswordAgainTextView.getText().toString();
        currentPasswordTextView.setError(null);
        newPasswordTextView.setError(null);
        newPasswordAgainTextView.setError(null);
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(oldPass)) {
            currentPasswordTextView.setError("Пароль не может быть пустым");
            focusView = currentPasswordTextView;
            cancel = true;
        }
        if (!isPasswordValid(oldPass)) {
            currentPasswordTextView.setError("Слишком короткий пароль");
            focusView = currentPasswordTextView;
            cancel = true;
        }

        if ((TextUtils.isEmpty(newPass))) {
            newPasswordTextView.setError("Слишком не может быть пустым");
            focusView = newPasswordTextView;
            cancel = true;
        }

        if (!isPasswordValid(newPass)) {
            newPasswordTextView.setError("Слишком короткий пароль");
            focusView = newPasswordTextView;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPassAgain)) {
            newPasswordAgainTextView.setError("Пароль не может быть пустым");
            focusView = newPasswordAgainTextView;
            cancel = true;
        }

        if (!isPasswordValid(newPassAgain)) {
            newPasswordAgainTextView.setError("Слишком короткий пароль");
            focusView = newPasswordAgainTextView;
            cancel = true;
        }

        if (!newPass.equals(newPassAgain)) {
            newPasswordAgainTextView.setError("Введенные новые пароли не совпадают.");
            focusView = newPasswordAgainTextView;
            cancel = true;
        }

        if(!cancel) {
            if (oldPass.equals(user.getPass())) {
                if (newPass.equals(newPassAgain)) {
                    Pair<String, String> httpParams1 = new Pair<>("pass", newPass);
                    Pair<String, String> httpParams2 = new Pair<>("oldpass", oldPass);
                    Pair<String, String> httpParams3 = new Pair<>("pass", newPass);
                    SettingsTask settingsTask = new SettingsTask(this, this);
                    settingsTask.setContext(context);
                    settingsTask.execute(httpParams1,httpParams2,httpParams3);

                } else {
                    Toast.makeText(context, "Введенные новые пароли не совпадают.", Toast.LENGTH_LONG).show();
                }
            } else {
                currentPasswordTextView.setError("Вы ввели не верный пароль, повторите попытку.");
                focusView = currentPasswordTextView;
                cancel = true;
            }
            currentPasswordTextView.clearComposingText();
            newPasswordTextView.clearComposingText();
            newPasswordAgainTextView.clearComposingText();
        }else{
            focusView.requestFocus();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private boolean isPhoneNumberValid(String phoneNumber){
        return phoneNumber.length()>6;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                profileBitmap = Common.getCroppedBitmap(profileBitmap, 604);
                photoImageView.setImageBitmap(profileBitmap);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                contentBody = new ByteArrayBody(bos.toByteArray(), "filename");
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
        if(task instanceof UploadPhotoTask) {
            if (task.isCancelled()) {
                // Report about cancel
                Toast.makeText(this, "Загрузка была отменена", Toast.LENGTH_LONG)
                        .show();
            } else {
                try {
                    if ((Integer) task.get() == 200) {
                        Toast.makeText(this, "Ваше фото отправленно на модерацию", Toast.LENGTH_LONG)
                                .show();
                        Common.encodePhotoPref(context, profileBitmap, "tempProfilePhoto");
                    } else {
                        Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }else if(task instanceof SettingsTask){
            if (task.isCancelled()) {
                Toast.makeText(this, "Операция отменена", Toast.LENGTH_LONG)
                        .show();
            } else {
                  try {
                       user = Common.getUserDataFromPref(this);
                      if(task.get()!=null){
                          if (task.get().equals("success")) {
                              if((int)o[0]==3) {
                                  String phone = formatPhoneNumber(user.getPhone());
                                  oldPhoneTextView.setText(phone);
                              }else if((int)o[0]==2){
                                  currentEmalTextView.setText(user.getEmail());
                              }else if((int)o[0]==4){
                                  SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                                  editor.putString("closedProfile", closedPrifileStr);
                                  editor.putString("searchableProfile", searchablePrifileStr);
                                  editor.apply();
                              }
                              Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_LONG)
                                      .show();
                          } else {
                              Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG)
                                      .show();
                          }
                      }else {
                          Toast.makeText(this, "Произошла ошибка (проверьте интернет соединение)", Toast.LENGTH_LONG)
                                  .show();
                      }



                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }else if(task instanceof FetchProfileTask){
            try {
                Pair<String, String> privacy = ((FetchProfileTask) task).get();
                if(privacy!=null){
                    closedProfile.setChecked(false);
                    hideProfile.setChecked(false);
                    if(privacy.getValue0().equals("1")){
                        closedProfile.setChecked(true);
                    }
                    if(privacy.getValue1().equals("1"))
                        hideProfile.setChecked(true);
                }else{
                    Toast.makeText(this, "Произошла ошибка (проверьте интернет соединение)", Toast.LENGTH_LONG)
                            .show();
                }


            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
