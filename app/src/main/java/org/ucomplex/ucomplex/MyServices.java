package org.ucomplex.ucomplex;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;

import org.ucomplex.ucomplex.Model.Users.User;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;

/**
 * Created by Sermilion on 03/12/2015.
 */
public class MyServices {

    public static int logingRole;
    public static HttpURLConnection connection;
    public static String lang_version;
    public static String X_UVERSION;
    public static int usersDataChanged=-1;
    public static String messageCompanionName = "-";

    public static String getLoginDataFromPref(Context mContext){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        User obj = gson.fromJson(json, User.class);
        return obj.getLogin()+":"+obj.getPass()+":"+obj.getRoles().get(0).getId();
    }

    public static User getUserDataFromPref(Context mContext){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public static void setUserDataToPref(Context mContext, User user){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("loggedUser", json);
        editor.apply();
    }

    public static Bitmap decodePhotoPref(Context context, String typeStr){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String encoded = pref.getString(typeStr, "");
        if(encoded.length()>0){
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), flags);
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            return photoBitmap;
        }
        return null;
    }

    public static void encodePhotoPref(Context context, Bitmap photoBitmap, String typeStr){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.URL_SAFE);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(typeStr, encoded);
        editor.apply();
    }

}

/**
 * logged
 * loggedUser
 * student
 * profilePhoto
 * tempProfilePhoto
 */
