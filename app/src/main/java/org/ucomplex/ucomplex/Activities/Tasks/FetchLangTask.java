package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Sermilion on 05/12/2015.
 */
public class FetchLangTask extends AsyncTask<Void, Void, Boolean> {

    Activity mContext;

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    private String sendPost() throws Exception {

        String url = "https://ucomplex.org/public/get_uc_vars?json&lang=1";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String jsonData = null;
        try {
            jsonData = sendPost();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            pref.edit().putString("lang", jsonData).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonData != null;
    }

}