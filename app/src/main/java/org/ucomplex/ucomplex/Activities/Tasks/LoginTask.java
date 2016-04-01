package org.ucomplex.ucomplex.Activities.Tasks;

/**
 * Created by Sermilion on 04/12/2015.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;

/**
 * Represents an asynchronous mLogin/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, User> {

    private final String mLogin;
    private final String mPassword;
    Activity mContext;
    Bitmap photoBitmap;

    public AsyncResponse delegate = null;

    public LoginTask(String email, String password, Activity _context) {
        mLogin = email;
        mPassword = password;
        mContext = _context;
    }

    private User getUserFromJson(String rolesJsonStr) throws JSONException {

        ArrayList<User> userRoles = new ArrayList<>();
        JSONObject rolesJson = new JSONObject(rolesJsonStr);
        JSONArray rolesArray = rolesJson.getJSONArray("roles");
        JSONObject roles;
        for (int i = 0; i < rolesArray.length(); i++) {
            roles = rolesArray.getJSONObject(i);
            User userRole = new User();
            userRole.setId(roles.getInt("id"));
            userRole.setName(roles.getString("name"));
            userRole.setPerson(roles.getInt("person"));
            userRole.setType(roles.getInt("type"));
            userRoles.add(userRole);
        }

        JSONObject userSession = rolesJson.getJSONObject("session");
        User user = new User();
        user.setType(-1);
        for (int i = 0; i < rolesArray.length(); i++) {
            if (rolesArray.getJSONObject(i).getInt("type") == 4) {
                user.setType(rolesArray.getJSONObject(i).getInt("type"));
                user.setId(rolesArray.getJSONObject(i).getInt("id"));
            }
        }
        user.setPerson(userSession.getInt("person"));
        user.setPhoto(userSession.getInt("photo"));
        user.setCode(userSession.getString("code"));
        user.setName(userSession.getString("name"));
        user.setClient(userSession.getInt("client"));
        user.setEmail(userSession.getString("email"));
        user.setLogin(userSession.getString("login"));
        user.setPass(userSession.getString("pass"));
        user.setPhone(userSession.getString("phone"));
        user.setSession(userSession.getString("session"));
        user.setRoles(userRoles);
        return user;
    }

    @Override
    protected User doInBackground(Void... params) {
        String urlString = "http://you.com.ru/auth?mobile=1";
        String jsonData = Common.httpPost(urlString, mLogin + ":" + mPassword);
        if (jsonData != null && !jsonData.equals("")) {
            User user = null;
            try {
                user = getUserFromJson(jsonData);
                if (user.getPhoto() == 1) {
                    photoBitmap = Common.getBitmapFromURL(user.getCode(), 0);
                    System.out.println();
                } else {
                    Common.deleteFromPref(mContext, "profilePhoto");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return user;
        }

        return new User();
    }

    @Override
    protected void onPostExecute(final User user) {
        delegate.processFinish(user, photoBitmap);
    }

    @Override
    protected void onCancelled() {
        delegate.canceled(true);
    }

    public interface AsyncResponse {
        void processFinish(User output, Bitmap bitmap);

        void canceled(boolean canceled);
    }
}