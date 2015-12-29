package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MyServices;
import org.ucomplex.ucomplex.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class SettingsActivity extends AppCompatActivity implements OnTaskCompleteListener {

    public static final int GET_FROM_GALLERY = 100;
    private Bitmap profileBitmap;
    ImageView photoImageView;
    SettingsActivity context = this;
    ByteArrayBody contentBody;
    boolean chose = false;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Настройки");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoImageView = (ImageView) findViewById(R.id.settings_photo);
        Bitmap photoBitmap = MyServices.decodePhotoPref(context, "tempProfilePhoto");
        photoImageView.setImageBitmap(photoBitmap);
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



            final TextView currentPasswordTextView = (TextView) findViewById(R.id.settings_password_current);
            final TextView newPasswordTextView = (TextView) findViewById(R.id.settings_password_new);
            final TextView newPasswordAgainTextView = (TextView) findViewById(R.id.settings_password_again);
            final User user = MyServices.getUserDataFromPref(this);

            Button changePassButton = (Button) findViewById(R.id.settings_password_reset_button);
            changePassButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final String oldPass = currentPasswordTextView.getText().toString();
                    final String newPass = newPasswordTextView.getText().toString();
                    final String newPassAgain = newPasswordAgainTextView.getText().toString();
                    currentPasswordTextView.setError(null);
                    newPasswordTextView.setError(null);
                    newPasswordAgainTextView.setError(null);
                    boolean cancel = false;
                    View focusView = null;

                    currentPasswordTextView.setError(null);
                    newPasswordTextView.setError(null);
                    newPasswordAgainTextView.setError(null);


                    if ((!TextUtils.isEmpty(oldPass) && !isPasswordValid(oldPass)) || TextUtils.isEmpty(oldPass)) {
                        currentPasswordTextView.setError("Слишком короткий пароль");
                        focusView = currentPasswordTextView;
                        cancel = true;
                    }

                    if ((!TextUtils.isEmpty(newPass) && !isPasswordValid(newPass)) || TextUtils.isEmpty(newPass)) {
                        newPasswordTextView.setError("Слишком короткий пароль");
                        focusView = newPasswordTextView;
                        cancel = true;
                    }

                    if ((!TextUtils.isEmpty(newPassAgain) && !isPasswordValid(newPassAgain)) || TextUtils.isEmpty(newPassAgain)) {
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
                                new AsyncTask<Void, Void, String>() {
                                    @Override
                                    protected String doInBackground(Void... params) {
                                        String url = "http://you.com.ru/student/profile/save";
                                        HashMap<String, String> httpParams = new HashMap<>();
                                        httpParams.put("oldpass", oldPass);
                                        httpParams.put("pass", newPass);
                                        String response = Common.httpPost(url, MyServices.getLoginDataFromPref(context), httpParams);
                                        try {
                                            JSONObject responseJson = new JSONObject(response);
                                            response = responseJson.getString("status");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return response;
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        if (s.equals("success")) {
                                            SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                                            prefsEditor.putBoolean("logged", false).apply();
                                            User user = MyServices.getUserDataFromPref(context);
                                            user.setPass(newPass);
                                            MyServices.setUserDataToPref(context, user);
                                            Toast.makeText(context, "Пароль был изменен.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute();
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
            });

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileBitmap = Common.getCroppedBitmap(profileBitmap, 604);
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
        if (task.isCancelled()) {
            // Report about cancel
            Toast.makeText(this, "Загрузка была отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                if((Integer)task.get()==200){
                    Toast.makeText(this, "Ваше фото отправленно на модерацию", Toast.LENGTH_LONG)
                            .show();
                    MyServices.encodePhotoPref(context, profileBitmap, "tempProfilePhoto");
                }else{
                    Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG)
                            .show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
