package org.ucomplex.ucomplex;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.impl.client.DefaultHttpClient;
import org.ucomplex.ucomplex.Model.Users.User;

import java.net.HttpURLConnection;

/**
 * Created by Sermilion on 03/12/2015.
 */
public class MyServices {

    public static int logingRole;
    public static HttpURLConnection connection;
    public static String lang_version;
    public static String X_UVERSION;

    public static String getLoginDataFromPref(Context mContext){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        User obj = gson.fromJson(json, User.class);
        return obj.getLogin()+":"+obj.getPass()+":"+obj.getRoles().get(0).getId();
    }

}
