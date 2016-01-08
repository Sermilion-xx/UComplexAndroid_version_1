package org.ucomplex.ucomplex.Activities.Tasks;

/**
 * Created by Sermilion on 04/12/2015.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.Student;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;

/**
 * Represents an asynchronous mLogin/registration task used to authenticate
 * the user.
 */
public class FetchUserLoginTask extends AsyncTask<Void, Void, Student> {

    private final String mLogin;
    private final String mPassword;
    Activity mContext;
    private String jsonData = null;
    Bitmap photoBitmap;

    public AsyncResponse delegate = null;

    public FetchUserLoginTask(String email, String password, Activity _context) {
        mLogin = email;
        mPassword = password;
        mContext = _context;
    }

    private Student getUserFromJson(String rolesJsonStr) throws JSONException {

        ArrayList<User> userRoles = new ArrayList<>();
        JSONObject rolesJson = new JSONObject(rolesJsonStr);
        JSONArray rolesArray = rolesJson.getJSONArray("roles");
        for(int i = 0; i < rolesArray.length(); i++) {
            JSONObject roles = rolesArray.getJSONObject(i);
            User userRole = new User();
            userRole.setId(roles.getInt("id"));
            userRole.setName(roles.getString("name"));
            userRole.setPerson(roles.getInt("person"));
            userRole.setType(roles.getInt("type"));
            userRoles.add(userRole);
        }
        JSONObject userSession = rolesJson.getJSONObject("session");
        Student student = new Student();

        student.setPhoto(userSession.getInt("photo"));
        student.setCode(userSession.getString("code"));
        student.setPerson(userSession.getInt("person"));
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
    protected Student doInBackground(Void... params) {
        String urlString = "http://you.com.ru/auth?mobile=1";
        jsonData = Common.httpPost(urlString, mLogin+":"+mPassword);
        if(jsonData!=null && !jsonData.equals("")) {
            Student student = null;
            try {
                student = getUserFromJson(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            photoBitmap = Common.getBitmapFromURL(student.getCode());
//            Common.encodePhotoPref(mContext, photoBitmap, "profilePhoto");
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String tempPhoto = "";
            tempPhoto = pref.getString("tempProfilePhoto", "");
            if (tempPhoto.length() < 1) {
                Common.encodePhotoPref(mContext, photoBitmap, "tempProfilePhoto");
            }
//            student.setPhotoBitmap(photoBitmap);
            return student;
        }

        return null;
    }

    @Override
    protected void onPostExecute(final Student student) {
        delegate.processFinish(student, photoBitmap);
    }

    @Override
    protected void onCancelled() {
        delegate.canceled(true);
    }

    public interface AsyncResponse {
        void processFinish(Student output, Bitmap  bitmap);
        void canceled(boolean canceled);
    }
}