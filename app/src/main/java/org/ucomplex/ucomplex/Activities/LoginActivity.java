package org.ucomplex.ucomplex.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ucomplex.ucomplex.Activities.Tasks.LoginTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.HashMap;

public class LoginActivity extends Activity implements LoginTask.AsyncResponse {

    private LoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mForgotButton;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean logged = prefs.getBoolean("logged", false);
        if (logged) {
            Intent intent = new Intent(this, EventsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedUserStr = prefs.getString("loggedUser", "");
        Gson gson = new Gson();
        User obj = gson.fromJson(loggedUserStr, User.class);
        String mLogin = "";
        String mPass = "";
        if (obj != null) {
            mLogin = obj.getLogin();
            mPass = obj.getPass();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mEmailView.setText(mLogin);
        mPasswordView.setText(mPass);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginSignInButton = (Button) findViewById(R.id.login_sign_in_button);
        mLoginSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mForgotButton = (Button) findViewById(R.id.forgot_pass_button);
        mForgotButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showInputDialog();
                    }
                });
    }

    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_forgot_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Common.isNetworkConnected(LoginActivity.this)) {
                            final String email = editText.getText().toString();
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    HashMap<String, String> postData = new HashMap<>();
                                    postData.put("email", email);
                                    String urlString = "https://chgu.org/public/password?mobile=1";
                                    Common.httpPost(urlString, Common.getLoginDataFromPref(LoginActivity.this), postData);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    Toast.makeText(LoginActivity.this, "На указанный адрес отправленна ссылка для восстановления пароля.", Toast.LENGTH_LONG).show();
                                }
                            }.execute();
                        } else {
                            Toast.makeText(LoginActivity.this, "Проверьте интернет соединение.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            mAuthTask = null;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new LoginTask(email, password, this);
            mAuthTask.delegate = this;
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
//89640633856
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void processFinish(User output, Bitmap bitmap) {
        if (output != null) {
            if(output.getType()!=4){
                showProgress(false);
                Toast.makeText(this, "Ошибка роли", Toast.LENGTH_SHORT).show();
            } else {
                Common.setUserDataToPref(this, output);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putBoolean("logged", true);
                editor.apply();

                if (bitmap != null) {
                    Common.encodePhotoPref(this, bitmap, "profilePhoto");
                }
                Intent intent;
                Common.setRoleToPref(this, output.getType());
//            if (output.getRoles().size() > 1) {
//                intent = new Intent(this, RoleSelectActivity.class);
//                startActivity(intent);
//                showProgress(false);
//            } else {
                intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
                showProgress(false);
//            }
                mAuthTask = null;
            }
        } else {
            showProgress(false);
            mPasswordView.setError("Неверные данные");
            mPasswordView.requestFocus();
        }
    }

    @Override
    public void canceled(boolean canceled) {
        showProgress(false);
        mAuthTask = null;
    }
}


