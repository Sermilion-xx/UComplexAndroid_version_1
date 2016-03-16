package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sermilion on 28/02/16.
 */
public class FetchAllStats extends AsyncTask<Integer, Void, ArrayList<Quintet<String, String, Double, Double, Integer>>> implements DialogInterface.OnCancelListener {

    Activity mContext;
    private final OnTaskCompleteListener mTaskCompleteListener;
    ArrayList<Quintet<String, String, Double, Double, Integer>> feedItems;

    public FetchAllStats(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public void setupTask(Integer... params) {
        this.execute(params);
    }

    @Override
    protected ArrayList<Quintet<String, String, Double, Double, Integer>> doInBackground(Integer... params) {
        String urlString = "http://you.com.ru/student/ajax/all_courses_stat?mobile=1";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        return getCalendarBeltDataFromJson(jsonData);
    }

    private ArrayList<Quintet<String, String, Double, Double, Integer>> getCalendarBeltDataFromJson(String jsonData) {
        JSONObject beltJson;
        feedItems = new ArrayList<>();
        try {
            if (jsonData != null) {
                beltJson = new JSONObject(jsonData);
                Integer group_table = beltJson.getInt("group_table");
                HashMap<String, String> coursesKV = (HashMap<String, String>) Common.parseJsonKV(beltJson.getJSONObject("courses"));
                JSONArray statisticArry = beltJson.getJSONArray("statistic");
                Quintet<String, String, Double, Double, Integer> item;
                for(int i = 0; i<statisticArry.length(); i++){
                    JSONObject statistic = statisticArry.getJSONObject(i);
                    double mark = 0.0;
                    if(statistic.getInt("mark")!=0 && statistic.getInt("marksCount")!=0){
                        mark = (double) statistic.getInt("mark")/(double) statistic.getInt("marksCount");
                    }
                    int a = statistic.getInt("absence");
                    int b = statistic.getInt("hours");
                    double absence = 0;
                    if (a != 0 && b != 0) {
                        absence = ((double) a / (double) b) * 100;
                    }
                    absence = 100.0 - absence;
                    DecimalFormat df = new DecimalFormat("#.##");
                    absence = Double.valueOf(df.format(absence));
                    mark = Double.valueOf(df.format(mark));
                    item = new Quintet<>(
                            statistic.getString("course"),
                            coursesKV.get(statistic.getString("course")),
                            mark,
                            absence,
                            group_table==statistic.getInt("table")?1:0);
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
