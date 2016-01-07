package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.javatuples.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;

/**
 * Created by Sermilion on 30/12/2015.
 */
public class FetchProfileTask extends AsyncTask<Void, Void, Pair<String, String>>{

    Activity mContext;
    private OnTaskCompleteListener mTaskCompleteListener;

    public FetchProfileTask(Activity mContext, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    @Override
    protected Pair<String, String> doInBackground(Void... params) {
        String urlString = "http://you.com.ru/user/profile?json";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject infoJson = jsonObject.getJSONObject("info");
            String closed = infoJson.getString("closed");
            String searchable  = infoJson.getString("searchable");
            Pair<String, String> privacy = new Pair<>(closed, searchable);
            return privacy;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Pair<String, String> objects) {
        super.onPostExecute(objects);
        mTaskCompleteListener.onTaskComplete(this);
    }

}
