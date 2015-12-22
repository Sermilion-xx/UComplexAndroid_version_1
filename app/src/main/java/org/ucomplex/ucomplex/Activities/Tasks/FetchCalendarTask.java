package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Calendar.CalendarEvent;
import org.ucomplex.ucomplex.Model.Calendar.ChangedDay;
import org.ucomplex.ucomplex.Model.Calendar.Lesson;
import org.ucomplex.ucomplex.Model.Calendar.Timetable;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class FetchCalendarTask extends AsyncTask<String, Void, UCCalendar> {

    Activity mContext;

    public FetchCalendarTask(){

    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected UCCalendar doInBackground(String... params) {
        String urlString = "http://you.com.ru/student/ajax/calendar?json";
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("month", String.valueOf(params[0]));
        postParams.put("time", String.valueOf(params[1]));
        String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext),postParams);
        return getCalendarDataFromJson(jsonData);
    }

    private UCCalendar getCalendarDataFromJson(String jsonData){
        JSONObject calendarJson = null;
        UCCalendar calendar = new UCCalendar();
        try {
            calendarJson = new JSONObject(jsonData);
            calendar.setMethod(calendarJson.getString("method"));
            //Start getting events
            JSONObject eventsJson = calendarJson.getJSONObject("events");

            for (int i = 0; i < eventsJson.length(); i++) {

                ArrayList<String> keys = getKeys(eventsJson);

                for(int j=0;j<eventsJson.length();j++) {
                    CalendarEvent calendarEvent = new CalendarEvent();
                    JSONArray subEvents = eventsJson.getJSONArray(keys.get(j));
                    for(int k=0;k<subEvents.length();k++){
                        JSONObject event = subEvents.getJSONObject(k);
                        calendarEvent.setDay(Integer.parseInt(keys.get(k)));
                        calendarEvent.setDescr(event.getString("descr"));
                        calendarEvent.setHoliday(event.getInt("holiday"));
                        calendarEvent.setStart(event.getString("start"));
                        calendarEvent.setTill(event.getString("till"));
                        calendar.addEvent(calendarEvent);
                    }
                }
            }
            //End getting events
            //------------------
            calendar.setAjax(calendarJson.getInt("ajax"));
            calendar.setYear(calendarJson.getString("year"));
            calendar.setMonth(calendarJson.getString("month"));
            calendar.setDay(calendarJson.getString("day"));
            calendar.setPre_month(calendarJson.getString("pre_month"));
            calendar.setNext_month(calendarJson.getString("next_month"));
            calendar.setGroup(calendarJson.getString("group"));
            calendar.setSubgroup(calendarJson.getString("subgroup"));
            calendar.setCourse(calendarJson.getString("course"));

            //Start getting courses
            JSONObject coursesObject = calendarJson.getJSONObject("courses");
            ArrayList<String> courseKeys = getKeys(coursesObject);

            for(int i=0;i<courseKeys.size();i++){
                HashMap<String, String> kvCourse = new HashMap<>();
                kvCourse.put(courseKeys.get(i),coursesObject.getString(courseKeys.get(i)));
                calendar.addCourse(kvCourse);
            }
            //End getting courses
            //------------------

            //Start getting changeDays
            JSONObject changeDaysJson = calendarJson.getJSONObject("changedDays");
            ArrayList<String> changeDaysKeys = getKeys(changeDaysJson);
            ChangedDay changeDay = new ChangedDay();
            for(int i=0;i<changeDaysKeys.size();i++){
                JSONObject changeDaysDay = changeDaysJson.getJSONObject(changeDaysKeys.get(i));
                for(int j = 1; j<changeDaysDay.length()+1;j++){
                    JSONObject lessonJson = changeDaysDay.getJSONObject(String.valueOf(j));
                    Lesson lesson = new Lesson();
                    lesson.setCourse(lessonJson.getInt("course"));
                    lesson.setNumber(j);
                    lesson.setType(lessonJson.getInt("type"));
                    lesson.setMark(lessonJson.getInt("mark"));
                    changeDay.addLesson(lesson);
                }
                changeDay.setDay(Integer.parseInt(changeDaysKeys.get(i)));
                calendar.addChangeDay(changeDay);
            }
            //End getting changeDays
            //---------------------

            //Start getting timetable---------------------------------------------------------------
            JSONObject timetableJson = calendarJson.getJSONObject("timetable");
            Timetable timetable = new Timetable();
            JSONObject teachersObject = timetableJson.getJSONObject("teachers");
            JSONObject hoursObject = timetableJson.getJSONObject("hours");
            JSONObject roomsObject = timetableJson.getJSONObject("rooms");
            JSONObject subjectsObject = timetableJson.getJSONObject("subjects");

            ArrayList<String> teachersKeys = getKeys(teachersObject);
            for(int i=0;i<teachersObject.length();i++){
                HashMap<String, String> kvTeacher = new HashMap<>();
                kvTeacher.put(teachersKeys.get(i),teachersObject.getString(teachersKeys.get(i)));
                timetable.addTeacher(kvTeacher);
            }

            for(int i=1;i<hoursObject.length()+1;i++){
                HashMap<String, String> kvHour = new HashMap<>();
                kvHour.put(String.valueOf(i),hoursObject.getString(String.valueOf(i)));
                timetable.addRoom(kvHour);
            }

            ArrayList<String> roomsKeys = getKeys(roomsObject);
            for(int i=0;i<roomsObject.length();i++){
                HashMap<String, String> kvRoom = new HashMap<>();
                kvRoom.put(roomsKeys.get(i),roomsObject.getString(roomsKeys.get(i)));
                timetable.addSubject(kvRoom);
            }

            ArrayList<String> subjectsKeys = getKeys(subjectsObject);
            for(int i=0;i<subjectsObject.length();i++){
                HashMap<String, String> kvSubject = new HashMap<>();
                kvSubject.put(subjectsKeys.get(i),subjectsObject.getString(subjectsKeys.get(i)));
                timetable.addSubject(kvSubject);
            }
            return calendar;
            }catch(JSONException e){
                e.printStackTrace();
            }

        return null;
        }

    private ArrayList<String> getKeys(JSONObject object) throws JSONException {
        ArrayList<String> keys = new ArrayList<>();
        Iterator iter = object.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            keys.add(key);
        }
        return keys;
    }

}
