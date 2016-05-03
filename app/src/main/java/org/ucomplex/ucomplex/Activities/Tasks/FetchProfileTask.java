package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.javatuples.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

/**
 * Created by Sermilion on 30/12/2015.
 */
public class FetchProfileTask extends AsyncTask<Void, Void, Pair<Pair<String, String>, JSONObject> >{

    Activity mContext;
    private OnTaskCompleteListener mTaskCompleteListener;

    public FetchProfileTask(Activity mContext, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    @Override
    protected Pair<Pair<String, String>, JSONObject> doInBackground(Void... params) {
        String urlString = "https://ucomplex.org/user/profile?json";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        if(jsonData!=null){
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject infoJson = jsonObject.getJSONObject("info");
                JSONObject infoCustomJson = jsonObject.getJSONObject("custom").getJSONObject("my_info");
                String closed = infoJson.getString("closed");
                String searchable  = infoJson.getString("searchable");
                Pair<String, String> privacy = new Pair<>(closed, searchable);
                Pair<Pair<String, String>, JSONObject> result = new Pair<>(privacy, infoCustomJson);
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Pair<Pair<String, String>, JSONObject> objects) {
        super.onPostExecute(objects);
        mTaskCompleteListener.onTaskComplete(this);
    }

}
