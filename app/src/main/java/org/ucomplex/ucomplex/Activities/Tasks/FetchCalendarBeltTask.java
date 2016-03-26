package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.javatuples.Quartet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Sermilion on 07/12/2015.
 */
public class FetchCalendarBeltTask extends AsyncTask<Integer, Void, ArrayList<Quartet<Integer, String, String, Integer>>> implements DialogInterface.OnCancelListener {

    Activity mContext;
    private final OnTaskCompleteListener mTaskCompleteListener;
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;
    private boolean fromCalendar;

    public FetchCalendarBeltTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public FetchCalendarBeltTask(Activity context) {
        this.mContext = context;
        mTaskCompleteListener = null;
    }


    public void setupTask(Integer... params) {
        this.execute(params);
    }

    @Override
    protected ArrayList<Quartet<Integer, String, String, Integer>> doInBackground(Integer... postParamsString) {
        String urlString = "http://you.com.ru/student/ajax/calendar_belt?json";
        HashMap<String, String> postParams = new HashMap<>();
        if (postParamsString.length == 1) {
            postParams.put("gcourse", String.valueOf(postParamsString[0]));
        } else if (postParamsString.length == 0) {
            fromCalendar = true;
        } else if (postParamsString.length == 2) {
            fromCalendar = true;
            postParams.put("gcourse", String.valueOf(postParamsString[0]));
            postParams.put("start", String.valueOf(postParamsString[1]));
        }
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), postParams);

        if(jsonData!=null){
            return getCalendarBeltDataFromJson(jsonData);
        }else{
            return new ArrayList<>();
        }
    }


    @Nullable
    private ArrayList<Quartet<Integer, String, String, Integer>> getCalendarBeltDataFromJson(String jsonData) {
        JSONObject courseJson;
        feedItems = new ArrayList<>();
        try {
            if (jsonData != null) {
                courseJson = new JSONObject(jsonData);
                HashMap<String, String> teachersMap = (HashMap<String, String>) Common.parseJsonKV(courseJson.getJSONObject("teachers"));
                HashMap<String, String> coursesMap = (HashMap<String, String>) Common.parseJsonKV(courseJson.getJSONObject("courses"));
                JSONArray marksArray = courseJson.getJSONArray("marks");

                for (int i = 0; i < marksArray.length(); i++) {

                    JSONObject marksItemJson = marksArray.getJSONObject(i);
                    int mark = marksItemJson.getInt("mark");
                    Date date = new java.util.Date((long) marksItemJson.getInt("time") * 1000);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf.format(date);
                    String teacherName = teachersMap.get(marksItemJson.getString("teacher"));
                    String courseName = coursesMap.get(marksItemJson.getString("course"));
                    String name;
                    if (fromCalendar) {
                        name = courseName;
                    } else {
                        name = teacherName;
                    }
                    Quartet<Integer, String, String, Integer> markItem = new Quartet<>(mark, name, time, marksItemJson.getInt("type"));
                    feedItems.add(markItem);
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
        if (mTaskCompleteListener != null) {
            mTaskCompleteListener.onTaskComplete(this);
        }
    }

    @Override
    protected void onPostExecute(ArrayList fileArrayList) {
        if (mTaskCompleteListener != null) {
            mTaskCompleteListener.onTaskComplete(this);
        }
    }
}
