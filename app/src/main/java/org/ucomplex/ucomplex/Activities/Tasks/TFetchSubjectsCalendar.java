package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.javatuples.Triplet;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.SubjectsActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Calendar.CalendarEvent;
import org.ucomplex.ucomplex.Model.Calendar.ChangedDay;
import org.ucomplex.ucomplex.Model.Calendar.Lesson;
import org.ucomplex.ucomplex.Model.Calendar.Timetable;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 21/04/16.
 */
public class TFetchSubjectsCalendar extends AsyncTask<String, String, UCCalendar> implements IProgressTracker, DialogInterface.OnCancelListener {

    Activity mContext;
    UCCalendar calendar;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private IProgressTracker mProgressTracker;

    public TFetchSubjectsCalendar(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    @Override
    protected UCCalendar doInBackground(String... params) {
        HashMap<String, String> postParams = new HashMap<>();
        String urlString = "https://ucomplex.org/teacher/ajax/my_subjects?mobile=1";
        postParams.put("subjId", params[0]);
        String jsonData;
        if(params.length>1){
            urlString = "https://ucomplex.org/teacher/ajax/attendance?mobile=1";
            postParams.put("month", String.valueOf(params[1]));
            postParams.put("time", String.valueOf(params[2]));
        }
        jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext),postParams);
        if(jsonData == null){
            return new UCCalendar();
        }
        return getCalendarDataFromJson(jsonData);
    }

    private UCCalendar getCalendarDataFromJson(String jsonData){
        JSONObject calendarJson;
        calendar = new UCCalendar();
        try {
            calendarJson = new JSONObject(jsonData);
            calendar.setSubjId(calendarJson.getInt("subjId"));
            calendar.setYear(calendarJson.getString("year"));
            calendar.setMonth(calendarJson.getString("month"));
            calendar.setDay(calendarJson.getString("day"));
            calendar.setPre_month(calendarJson.getString("pre_month"));
            calendar.setNext_month(calendarJson.getString("next_month"));
            calendar.setGroup(calendarJson.getString("group"));
            calendar.setCourse(calendarJson.getString("course"));

            //Start getting changeDays
            try {
                JSONObject changeDaysJson = calendarJson.getJSONObject("changedDays");
                ArrayList<String> changeDaysKeys = Common.getKeys(changeDaysJson);
                for(int i=0;i<changeDaysKeys.size();i++){
                    ChangedDay changeDay = new ChangedDay();
                    JSONObject changeDaysDay = changeDaysJson.getJSONObject(changeDaysKeys.get(i));
                    for(int j = 1; j<changeDaysDay.length()+1;j++){
                        JSONObject lessonJson = changeDaysDay.getJSONObject(String.valueOf(j));
                        Lesson lesson = new Lesson();
                        lesson.setCourse(lessonJson.getInt("course"));
                        lesson.setNumber(j+1);
                        lesson.setType(lessonJson.getInt("type"));
                        lesson.setMark(lessonJson.getInt("mark"));
                        changeDay.addLesson(lesson);
                    }
                    changeDay.setDay(Integer.parseInt(changeDaysKeys.get(i)));
                    calendar.addChangeDay(changeDay);
                }
            }catch (JSONException ignored){}
        }catch (JSONException e){
            e.printStackTrace();
        }
        calendar.setTimetable(new Timetable());
        calendar.setEvents(new ArrayList<CalendarEvent>());
        return calendar;
    }

    @Override
    protected void onPostExecute(UCCalendar fileArrayList) {
        super.onPostExecute(fileArrayList);
        mTaskCompleteListener.onTaskComplete(this);
    }

    /* UI Thread */
    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        mProgressTracker = null;
    }

    @Override
    public void onProgress(String message) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
    }
}
