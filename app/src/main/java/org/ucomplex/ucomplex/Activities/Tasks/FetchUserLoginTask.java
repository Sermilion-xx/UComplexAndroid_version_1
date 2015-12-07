package org.ucomplex.ucomplex.Activities.Tasks;

/**
 * Created by Sermilion on 04/12/2015.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Role;
import org.ucomplex.ucomplex.Model.Users.Student;
import java.util.ArrayList;

/**
 * Represents an asynchronous mLogin/registration task used to authenticate
 * the user.
 */
public class FetchUserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mLogin;
    private final String mPassword;
    Activity mContext;
    private String jsonData = null;

    public AsyncResponse delegate = null;

    public FetchUserLoginTask(String email, String password, Activity _context) {
        mLogin = email;
        mPassword = password;
        mContext = _context;
    }

    private Student getUserFromJson(String rolesJsonStr) throws JSONException {

        ArrayList<Role> userRoles = new ArrayList<>();
        JSONObject rolesJson = new JSONObject(rolesJsonStr);
        JSONArray rolesArray = rolesJson.getJSONArray("roles");
        for(int i = 0; i < rolesArray.length(); i++) {
            JSONObject roles = rolesArray.getJSONObject(i);
            Role userRole = new Role();
            userRole.setId(roles.getString("id"));
            userRole.setName(roles.getString("person"));
            userRole.setPerson(roles.getString("person"));
            userRole.setType(roles.getInt("type"));
            userRoles.add(userRole);
        }
        JSONObject userSession = rolesJson.getJSONObject("session");
        Student student = new Student();
        student.setPhoto(userSession.getInt("photo"));
        student.setCode(userSession.getString("code"));
        student.setPerson(userSession.getString("person"));
        student.setName(userSession.getString("name"));
        student.setClient(userSession.getInt("client"));
        student.setEmail(userSession.getString("email"));
        student.setLogin(userSession.getString("login"));
        student.setPass(userSession.getString("pass"));
        student.setPhone(userSession.getString("phone"));
        student.setSession(userSession.getString("session"));
        student.setRoles(userRoles);
        return student;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (params.length == 0) {
            return null;
        }
        String urlString = "http://you.com.ru/auth?json";
        jsonData = Common.httpPost(urlString, mLogin+":"+mPassword);
        return jsonData != null;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            try {
            Student student = getUserFromJson(jsonData);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                Gson gson = new Gson();
                String json = gson.toJson(student);
                editor.putString("loggedUser", json);
            editor.apply();

                delegate.processFinish(student);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @Override
    protected void onCancelled() {
        delegate.canceled(true);
    }

    public interface AsyncResponse {
        void processFinish(Student output);
        void canceled(boolean canceled);
    }
}