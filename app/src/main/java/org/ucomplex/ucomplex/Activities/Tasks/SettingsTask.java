package org.ucomplex.ucomplex.Activities.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.javatuples.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MyServices;

import java.util.HashMap;

/**
 * Created by Sermilion on 29/12/2015.
 */
public class SettingsTask extends AsyncTask<Pair<String, String>, Void, String> {

    private Context context;
    Pair<String, String> change;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Pair<String, String>... params) {
        String url = "http://you.com.ru/student/profile/save";
        change = params[2];
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put(params[0].getValue0(), params[0].getValue1());
        httpParams.put(params[1].getValue0(), params[1].getValue1());
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
            if(change.getValue0().equals("phone")){
                user.setPhone(change.getValue1());
            }else if(change.getValue0().equals("email")){
                user.setEmail(change.getValue1());
            }else if(change.getValue0().equals("pass")){
                user.setPass(change.getValue1());
            }
            MyServices.setUserDataToPref(context, user);
            Toast.makeText(context, "Пароль был изменен.", Toast.LENGTH_LONG).show();
        }
    }
}
