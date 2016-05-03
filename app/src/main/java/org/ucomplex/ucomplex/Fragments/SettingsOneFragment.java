package org.ucomplex.ucomplex.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.javatuples.Pair;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.SettingsActivity2;
import org.ucomplex.ucomplex.Activities.Tasks.FetchProfileTask;
import org.ucomplex.ucomplex.Activities.Tasks.SettingsTask;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.CustomImageViewCircularShape;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 29/02/16.
 */
public class SettingsOneFragment extends Fragment implements OnTaskCompleteListener {

    public static boolean PROFILE_IMAGE_CHANGED;
    public static boolean CURRENT_PASSWORD_CHANGE;
    public static boolean NEW_PASSWORD_CHANGE;
    public static boolean NEW_PASSWORD_AGAIN_CHANGE;

    public static boolean NEW_EMAIL_CHANGE;
    public static boolean NEW_EMAIL_PASSWORD_CHANGE;

    public static boolean NEW_PHONE_CHANGED;
    public static boolean NEW_PHONE_PASSWORD_CHANGE;

    public static boolean PRIVACY_CHANGED;


    private Bitmap profileBitmap;
    ImageView photoImageView;
    SettingsActivity2 context;
    ByteArrayBody contentBody;
    boolean chose = false;
    public User user;
    String filename;

    public TextView currentEmalTextView;
    public TextView newEmalTextView;
    public TextView passwordEmalTextView;
    public String closedPrifileStr;
    public String searchablePrifileStr;

    public Switch closedProfile;
    public Switch hideProfile;
    Button privacyButton;
    CustomImageViewCircularShape changePhotoButton;
    Typeface robotoFont;

    public TextView currentPasswordTextView;
    public TextView newPasswordTextView;
    public TextView newPasswordAgainTextView;

    public TextView newPhoneTextView;
    public TextView oldPasswordPhoneTextView;
    public TextView oldPhoneTextView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (currentPasswordTextView.getText().hashCode() == s.hashCode()) {
                CURRENT_PASSWORD_CHANGE = true;
            } else if (newPasswordTextView.getText().hashCode() == s.hashCode()) {
                NEW_PASSWORD_CHANGE = true;
            } else if (newPasswordAgainTextView.getText().hashCode() == s.hashCode()) {
                NEW_PASSWORD_AGAIN_CHANGE = true;
            } else if (newEmalTextView.getText().hashCode() == s.hashCode()) {
                NEW_EMAIL_CHANGE = true;
            } else if (passwordEmalTextView.getText().hashCode() == s.hashCode()) {
                NEW_EMAIL_PASSWORD_CHANGE = true;
            } else if (newPhoneTextView.getText().hashCode() == s.hashCode()) {
                NEW_PHONE_CHANGED = true;
            } else if (oldPasswordPhoneTextView.getText().hashCode() == s.hashCode()) {
                NEW_PHONE_PASSWORD_CHANGE = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_one, container, false);
        if (context == null) {
            robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        } else {
            robotoFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        }
        //Photo
        profileBitmap = Common.decodePhotoPref(context, "profilePhoto");
        user = Common.getUserDataFromPref(context);
        FetchProfileTask fetchProfileTask = new FetchProfileTask(context, context);
        fetchProfileTask.execute();
        photoImageView = (ImageView) rootView.findViewById(R.id.settings_photo);
        changePhotoButton = (CustomImageViewCircularShape) rootView.findViewById(R.id.setting_button_changeImage);

        currentPasswordTextView = (TextView) rootView.findViewById(R.id.settings_password_current);
        newPasswordTextView = (TextView) rootView.findViewById(R.id.settings_password_new);
        newPasswordAgainTextView = (TextView) rootView.findViewById(R.id.settings_password_again);

        newPhoneTextView = (TextView) rootView.findViewById(R.id.settings_phone_new);
        oldPasswordPhoneTextView = (TextView) rootView.findViewById(R.id.settings_phone_password_current_phone);

        newEmalTextView = (TextView) rootView.findViewById(R.id.settings_email_new);
        currentEmalTextView = (TextView) rootView.findViewById(R.id.settings_email_current);
        passwordEmalTextView = (TextView) rootView.findViewById(R.id.settings_email_password);

        //Phone settings
        newPhoneTextView = (TextView) rootView.findViewById(R.id.settings_phone_new);
        oldPasswordPhoneTextView = (TextView) rootView.findViewById(R.id.settings_phone_password_current_phone);
        oldPhoneTextView = (TextView) rootView.findViewById(R.id.settings_phone_number);
//        String phone = formatPhoneNumber(user.getPhone());
//        oldPhoneTextView.setText(phone);
//        Button changePhoneButton = (Button) findViewById(R.id.settings_phone_reset_button);
//        changePhoneButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                changePhoneNumber(newPhoneTextView, oldPasswordPhoneTextView);
//            }
//        });

        //Password settings
        currentPasswordTextView.setTypeface(robotoFont);
        currentPasswordTextView.addTextChangedListener(textWatcher);
        newPasswordTextView.setTypeface(robotoFont);
        newPasswordTextView.addTextChangedListener(textWatcher);
        newPasswordAgainTextView.setTypeface(robotoFont);
        newPasswordAgainTextView.addTextChangedListener(textWatcher);

        //Phone settings
        newPhoneTextView.setTypeface(robotoFont);
        newPhoneTextView.addTextChangedListener(textWatcher);
        oldPasswordPhoneTextView.setTypeface(robotoFont);
        oldPasswordPhoneTextView.addTextChangedListener(textWatcher);
        oldPhoneTextView.setTypeface(robotoFont);
        oldPhoneTextView.addTextChangedListener(textWatcher);
        oldPhoneTextView.setText(user.getPhone());

        //Email setting
        currentEmalTextView.setTypeface(robotoFont);
        currentEmalTextView.addTextChangedListener(textWatcher);
        currentEmalTextView.setText(user.getEmail());
        passwordEmalTextView.setTypeface(robotoFont);
        passwordEmalTextView.addTextChangedListener(textWatcher);
        newEmalTextView.setTypeface(robotoFont);
        newEmalTextView.addTextChangedListener(textWatcher);

        closedProfile = (Switch) rootView.findViewById(R.id.settings_privacy_closed_profile);
        hideProfile = (Switch) rootView.findViewById(R.id.settings_privacy_hide_profile);
        closedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkConnected(context)) {
                    changePrivacy(closedProfile, hideProfile);
                } else {
                    Toast.makeText(context, "Проверьте интернет соединение.", Toast.LENGTH_LONG).show();
                }
            }
        });
        hideProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkConnected(context)) {
                    changePrivacy(closedProfile, hideProfile);
                } else {
                    Toast.makeText(context, "Проверьте интернет соединение.", Toast.LENGTH_LONG).show();
                }
            }
        });


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        closedPrifileStr = pref.getString("closedProfile", "0");
        searchablePrifileStr = pref.getString("searchableProfile", "0");
        if (closedPrifileStr.equals("1"))
            closedProfile.setChecked(true);
        if (searchablePrifileStr.equals("1"))
            hideProfile.setChecked(true);

//        Bitmap bmp = null;
//
//        try {
//            FileInputStream is = context.openFileInput(filename);
//            bmp = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (profileBitmap == null) {
            final int colorsCount = 16;
            final int number = (user.getPerson() <= colorsCount) ? user.getPerson() : user.getPerson() % colorsCount;
            char firstLetter = user.getName().split(" ")[1].charAt(0);
            TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .width(1000)
                    .height(604)
                    .endConfig()
                    .buildRect(String.valueOf(firstLetter), Common.getColor(number));
            photoImageView.setImageDrawable(drawable);
        } else {
            photoImageView.setImageBitmap(profileBitmap);
        }
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getActivity().startActivityForResult(photoPickerIntent, SettingsActivity2.GET_FROM_GALLERY);
                chose = true;
            }
        });
        return rootView;
    }

    public void changePrivacy(Switch closedPrifile, Switch hideProfile) {
        if (closedPrifile.isChecked())
            closedPrifileStr = "1";
        else
            closedPrifileStr = "0";
        if (hideProfile.isChecked())
            searchablePrifileStr = "1";
        else
            searchablePrifileStr = "0";

        Pair<String, String> httpParams1 = new Pair<>("closed", closedPrifileStr);
        Pair<String, String> httpParams2 = new Pair<>("searchable", searchablePrifileStr);
        Pair<String, String> httpParams3 = new Pair<>("privacy", "");
        SettingsTask settingsTask = new SettingsTask(context, context);
        settingsTask.setContext(context);
        settingsTask.execute(httpParams1, httpParams2, httpParams3);
    }

    public void changeEmail(TextView passwordEmalTextView, TextView newEmalTextView) {
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
        }else if (!currentPassword.equals(user.getPass())) {
            passwordEmalTextView.setError("Неверный пароль");
            focusView = passwordEmalTextView;
            cancel = true;
        }

        if (!cancel) {
            Pair<String, String> httpParams1 = new Pair<>("email", newEmal);
            Pair<String, String> httpParams2 = new Pair<>("currpass", currentPassword);
            Pair<String, String> httpParams3 = new Pair<>("email", newEmal);
            SettingsTask settingsTask = new SettingsTask(context, context);
            settingsTask.setContext(context);
            settingsTask.execute(httpParams1, httpParams2, httpParams3);
        } else {
            focusView.requestFocus();
        }

    }

    private boolean isEmailValid(String email) {
        if (email.contains("@")) {
            String[] splitEmail = email.split("@");
            if (splitEmail.length == 2) {
                if (splitEmail[1].contains(".")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String formatPhoneNumber(String phoneNumber) {
        return phoneNumber.replace(" ", "").replaceAll("\\b(\\d{4})\\d+(\\d{2})", "$1*****$2");
    }


    public void changePhoneNumber(TextView newPhoneTextView, TextView oldPasswordPhoneTextView) {
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
        }else if (newPhoneNumber.length() > 0) {
            if (newPhoneNumber.charAt(0) != '+') {
                newPhoneTextView.setError("Номер начинается с \"+\"");
                focusView = newPhoneTextView;
                cancel = true;
            }
        }

        if ((TextUtils.isEmpty(currentPassword))) {
            oldPasswordPhoneTextView.setError("Введите пароль");
            focusView = oldPasswordPhoneTextView;
            cancel = true;
        }else if (!isPasswordValid(currentPassword)) {
            oldPasswordPhoneTextView.setError("Слишком короткий пароль");
            focusView = oldPasswordPhoneTextView;
            cancel = true;
        }else  if (!currentPassword.equals(user.getPass())) {
            oldPasswordPhoneTextView.setError("Неверный пароль");
            focusView = oldPasswordPhoneTextView;
            cancel = true;
        }

        if (!cancel) {
            Pair<String, String> httpParams1 = new Pair<>("phone", newPhoneNumber);
            Pair<String, String> httpParams2 = new Pair<>("currpass", currentPassword);
            Pair<String, String> httpParams3 = new Pair<>("phone", newPhoneNumber);
            SettingsTask settingsTask = new SettingsTask(context, context);
            settingsTask.setContext(context);
            settingsTask.execute(httpParams1, httpParams2, httpParams3);
        } else {
            focusView.requestFocus();
        }

    }

    public void resetPassword(TextView currentPasswordTextView, TextView newPasswordTextView, TextView newPasswordAgainTextView,
                              User user) {
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
        }else if (!isPasswordValid(oldPass)) {
            currentPasswordTextView.setError("Слишком короткий пароль");
            focusView = currentPasswordTextView;
            cancel = true;
        }

        if ((TextUtils.isEmpty(newPass))) {
            newPasswordTextView.setError("Пароль не может быть пустым");
            focusView = newPasswordTextView;
            cancel = true;
        }else if (!isPasswordValid(newPass)) {
            newPasswordTextView.setError("Слишком короткий пароль");
            focusView = newPasswordTextView;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPassAgain)) {
            newPasswordAgainTextView.setError("Пароль не может быть пустым");
            focusView = newPasswordAgainTextView;
            cancel = true;
        }else if (!isPasswordValid(newPassAgain)) {
            newPasswordAgainTextView.setError("Слишком короткий пароль");
            focusView = newPasswordAgainTextView;
            cancel = true;
        }

        if (!newPass.equals(newPassAgain)) {
            newPasswordAgainTextView.setError("Введенные новые пароли не совпадают.");
            focusView = newPasswordAgainTextView;
            cancel = true;
        }

        if (!cancel) {
            if (oldPass.equals(user.getPass())) {
                if (newPass.equals(newPassAgain)) {
                    Pair<String, String> httpParams1 = new Pair<>("pass", newPass);
                    Pair<String, String> httpParams2 = new Pair<>("oldpass", oldPass);
                    Pair<String, String> httpParams3 = new Pair<>("pass", newPass);
                    SettingsTask settingsTask = new SettingsTask(context, context);
                    settingsTask.setContext(context);
                    settingsTask.execute(httpParams1, httpParams2, httpParams3);

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
        } else {
            focusView.requestFocus();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() > 6;
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task instanceof UploadPhotoTask) {
            if (task.isCancelled()) {
                Toast.makeText(context, "Загрузка была отменена", Toast.LENGTH_LONG).show();
            } else {
                try {
                    if ((Integer) task.get() == 200) {
                        Toast.makeText(context, "Ваше фото отправленно на модерацию", Toast.LENGTH_LONG).show();
                        Common.encodePhotoPref(context, profileBitmap, "tempProfilePhoto");
                    } else {
                        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else if (task instanceof FetchProfileTask) {
            try {
                Pair<Pair<String, String>, JSONObject> profileSettings = ((FetchProfileTask) task).get();
                Pair<String, String> privacy = profileSettings.getValue0();
                if (privacy != null) {
                    closedProfile.setChecked(false);
                    hideProfile.setChecked(false);
                    if (privacy.getValue0().equals("1")) {
                        closedProfile.setChecked(true);
                    }
                    if (privacy.getValue1().equals("1"))
                        hideProfile.setChecked(true);
                } else {
                    Toast.makeText(context, "Произошла ошибка (проверьте интернет соединение)", Toast.LENGTH_LONG)
                            .show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    public ByteArrayBody getContentBody() {
        return contentBody;
    }

    public void setContentBody(ByteArrayBody contentBody) {
        this.contentBody = contentBody;
    }

    public void setProfileBitmap(Bitmap profileBitmap) {
        this.profileBitmap = profileBitmap;
    }

    public Bitmap getProfileBitmap() {
        return profileBitmap;
    }

    public ImageView getPhotoImageView() {
        return photoImageView;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setContext(SettingsActivity2 context) {
        this.context = context;
    }

}