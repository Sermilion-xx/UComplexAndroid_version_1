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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



/**
 * Created by Sermilion on 07/12/2015.
 */
public class FetchCalendarBeltTask extends AsyncTask<Integer, Void, ArrayList<Quartet<Integer, String, String, Integer>>> implements DialogInterface.OnCancelListener{

    Activity mContext;
    private final OnTaskCompleteListener mTaskCompleteListener;
    ArrayList<Quartet<Integer, String, String, Integer>> feedItems;

    public FetchCalendarBeltTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public void setupTask(Integer ... params) {
        this.execute(params);
    }

    @Override
    protected ArrayList<Quartet<Integer, String, String, Integer>> doInBackground(Integer... postParamsString) {
        String urlString = "http://you.com.ru/student/ajax/calendar_belt?json";
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("gcourse", String.valueOf(postParamsString[0]));
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), postParams);
        return getCalendarBeltDataFromJson(jsonData);
    }



    private ArrayList<Quartet<Integer, String, String, Integer>> getCalendarBeltDataFromJson(String jsonData) {
        JSONObject courseJson;
        feedItems = new ArrayList<>();
        try {
            courseJson = new JSONObject(jsonData);
            JSONObject teachers = courseJson.getJSONObject("teachers");
            HashMap<String, String> teachersMap = (HashMap<String, String>) Common.parseJsonKV(teachers);
            JSONArray marksArray = courseJson.getJSONArray("marks");

            for(int i=0;i<marksArray.length();i++){

                JSONObject marksItemJson = marksArray.getJSONObject(i);
                int mark = marksItemJson.getInt("mark");
                Date date=new java.util.Date((long)marksItemJson.getInt("time")*1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(date);
                String teacherName = teachersMap.get(marksItemJson.getString("teacher"));
                Quartet<Integer, String, String, Integer>markItem = new Quartet<>(mark, teacherName,time, marksItemJson.getInt("type"));
                feedItems.add(markItem);
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
