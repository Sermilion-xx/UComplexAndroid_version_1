package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.javatuples.Quartet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.MyServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



/**
 * Created by Sermilion on 07/12/2015.
 */
public class FetchCalendarBeltTask extends AsyncTask<Integer, Void, ArrayList<Quartet<Integer, String, String, Integer>>> {

    Activity mContext;

    public FetchCalendarBeltTask(){

    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Quartet<Integer, String, String, Integer>> doInBackground(Integer... postParamsString) {
        String urlString = "http://you.com.ru/student/ajax/calendar_belt?json";
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("gcourse", String.valueOf(postParamsString[0]));
        String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext), postParams);
        return getCalendarBeltDataFromJson(jsonData);
    }



    private ArrayList<Quartet<Integer, String, String, Integer>> getCalendarBeltDataFromJson(String jsonData) {
        JSONObject courseJson = null;
        ArrayList<Quartet<Integer, String, String, Integer>> feedItem = new ArrayList<>();
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
                feedItem.add(markItem);
            }
            return feedItem;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
