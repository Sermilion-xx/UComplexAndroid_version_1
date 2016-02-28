package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.javatuples.Quartet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 28/02/16.
 */
public class FetchAllStats extends AsyncTask<Integer, Void, ArrayList<Quartet<String, String, String, String>>> implements DialogInterface.OnCancelListener {

    Activity mContext;
    private final OnTaskCompleteListener mTaskCompleteListener;
    ArrayList<Quartet<String, String, String, String>> feedItems;

    public FetchAllStats(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public void setupTask(Integer... params) {
        this.execute(params);
    }

    @Override
    protected ArrayList<Quartet<String, String, String, String>> doInBackground(Integer... params) {
        String urlString = "http://you.com.ru/student/ajax/all_courses_stat?mobile=1";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        return getCalendarBeltDataFromJson(jsonData);
    }

    private ArrayList<Quartet<String, String, String, String>> getCalendarBeltDataFromJson(String jsonData) {
        JSONObject beltJson;
        feedItems = new ArrayList<>();
        try {
            if (jsonData != null) {
                beltJson = new JSONObject(jsonData);
                HashMap<String, String> coursesKV = (HashMap<String, String>) Common.parseJsonKV(beltJson.getJSONObject("courses"));
                JSONArray statisticArry = beltJson.getJSONArray("statistic");
                for(int i = 0; i<statisticArry.length(); i++){
                    JSONObject statistic = statisticArry.getJSONObject(i);
                    Quartet<String, String, String, String> item = new Quartet<>(
                            statistic.getString("course"),
                            coursesKV.get(statistic.getString("course")),
                            statistic.getString("absence"),
                            statistic.getString("_mark"));
                    feedItems.add(item);
                }
            }
            return feedItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    protected void onPostExecute(ArrayList fileArrayList) {
        mTaskCompleteListener.onTaskComplete(this);
    }
}
