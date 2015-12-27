package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.CalendarActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Calendar.CalendarEvent;
import org.ucomplex.ucomplex.Model.Calendar.ChangedDay;
import org.ucomplex.ucomplex.Model.Calendar.Lesson;
import org.ucomplex.ucomplex.Model.Calendar.Timetable;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class FetchCalendarTask extends AsyncTask<String, String, UCCalendar> {

    Activity mContext;

    CalendarActivity caller;
    UCCalendar calendar;

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;

    /* UI Thread */
    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        mProgressTracker = null;
    }

    /* UI Thread */
    @Override
    protected void onProgressUpdate(String... values) {
        // Update progress message
        mProgressMessage = values[0];
        // And send it to progress tracker
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
        }
    }


    public FetchCalendarTask(){

    }
    public FetchCalendarTask(Activity context){
        this.mContext = context;
        this.caller = (CalendarActivity) mContext;
    }

    public Activity getmContext() {
        return mContext;
    }

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем календарь");
            if (calendar != null) {
                mProgressTracker.onComplete();
            }
        }
    }


    @Override
    protected UCCalendar doInBackground(String... params) {

        String urlString = "http://you.com.ru/student/ajax/calendar?json";
        HashMap<String, String> postParams;
        String jsonData;
        if(params.length>0){
            postParams = new HashMap<>();
            postParams.put("month", String.valueOf(params[0]));
            postParams.put("time", String.valueOf(params[1]));
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext),postParams);
        } else {
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        }
        publishProgress("50%");
        return getCalendarDataFromJson(jsonData);
    }

    private UCCalendar getCalendarDataFromJson(String jsonData){
        JSONObject calendarJson = null;
        calendar = new UCCalendar();
        try {
            calendarJson = new JSONObject(jsonData);
            calendar.setMethod(calendarJson.getString("method"));
            //Start getting events
            try {
                JSONObject eventsJson = calendarJson.getJSONObject("events");

                for (int i = 0; i < eventsJson.length(); i++) {
                    ArrayList<String> keys = Common.getKeys(eventsJson);
                        CalendarEvent calendarEvent = new CalendarEvent();
                        JSONArray subEvents = eventsJson.getJSONArray(keys.get(i));
                        for (int k = 0; k < subEvents.length(); k++) {
                            JSONObject event = subEvents.getJSONObject(k);
                            calendarEvent.setDay(Integer.parseInt(keys.get(i)));
                            calendarEvent.setDescr(event.getString("descr"));
                            calendarEvent.setName(event.getString("name"));
                            calendarEvent.setHoliday(event.getInt("holiday"));
                            String[] dateArray = event.getString("start").split("-");
                            String day1 = String.valueOf(calendarEvent.getDay()>9 ? calendarEvent.getDay() : "0"+calendarEvent.getDay());
                            calendarEvent.setStart(dateArray[0]+"-"+dateArray[1]+"-"+day1);
                            calendarEvent.setTill(event.getString("till"));
                            calendar.addEvent(calendarEvent);
                        }
                }
            }catch (JSONException ignored){

            }

            //End getting events
            //------------------
            try {
                calendar.setAjax(calendarJson.getInt("ajax"));
            }catch (JSONException ignored){}

            calendar.setYear(calendarJson.getString("year"));
            calendar.setMonth(calendarJson.getString("month"));
            calendar.setDay(calendarJson.getString("day"));
            calendar.setPre_month(calendarJson.getString("pre_month"));
            calendar.setNext_month(calendarJson.getString("next_month"));
            calendar.setGroup(calendarJson.getString("group"));
            calendar.setSubgroup(calendarJson.getString("subgroup"));
            calendar.setCourse(calendarJson.getString("course"));

            //Start getting courses
            try {
                JSONObject coursesObject = calendarJson.getJSONObject("courses");
                ArrayList<String> courseKeys = Common.getKeys(coursesObject);
                HashMap<String, String> kvCourse = new HashMap<>();

                for (int i = 0; i < courseKeys.size(); i++) {
                    kvCourse.put(courseKeys.get(i), coursesObject.getString(courseKeys.get(i)));
                }
                calendar.setCourses(kvCourse);
            }catch (JSONException ignored){}
            //End getting courses
            //------------------

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
            //End getting changeDays
            //---------------------

            //Start getting timetable---------------------------------------------------------------
            JSONObject timetableJson = calendarJson.getJSONObject("timetable");
            Timetable timetable = new Timetable();
            JSONObject teachersObject = timetableJson.getJSONObject("teachers");
            JSONObject hoursObject = timetableJson.getJSONObject("hours");
            JSONObject roomsObject = timetableJson.getJSONObject("rooms");
            JSONObject subjectsObject = timetableJson.getJSONObject("subjects");


            ArrayList<String> teachersKeys = Common.getKeys(teachersObject);
            HashMap<String, String> kvTeacher = new HashMap<>();
            for(int i=0;i<teachersObject.length();i++){
                kvTeacher.put(teachersKeys.get(i),teachersObject.getString(teachersKeys.get(i)));
            }
            timetable.setTeachers(kvTeacher);

            HashMap<String, String> kvHour = new HashMap<>();
            for(int i=1;i<hoursObject.length()+1;i++){
                kvHour.put(String.valueOf(i),hoursObject.getString(String.valueOf(i)));
            }
            timetable.setHours(kvHour);

            ArrayList<String> roomsKeys = Common.getKeys(roomsObject);
            HashMap<String, String> kvRoom = new HashMap<>();
            for(int i=0;i<roomsObject.length();i++){
                kvRoom.put(roomsKeys.get(i),roomsObject.getString(roomsKeys.get(i)));
            }
            timetable.setRooms(kvRoom);

            ArrayList<String> subjectsKeys = Common.getKeys(subjectsObject);
            HashMap<String, String> kvSubject = new HashMap<>();
            for(int i=0;i<subjectsObject.length();i++){
                kvSubject.put(subjectsKeys.get(i),subjectsObject.getString(subjectsKeys.get(i)));
            }
            timetable.setSubjects(kvSubject);

            try {
                JSONObject entriesObject = timetableJson.getJSONObject("entries");
                ArrayList<String> entriesKeys = Common.getKeys(entriesObject);
                ArrayList<HashMap<String, String>> entries = new ArrayList<>();

                for (int i = 0; i < entriesKeys.size(); i++) {
                    JSONArray entrieJson = entriesObject.getJSONArray(entriesKeys.get(i));
                    HashMap<String, String> kvEntry = new HashMap<>();
                    for (int j = 0; j < entrieJson.length(); j++) {
                        JSONObject subEntryJSON = entrieJson.getJSONObject(j);
                        kvEntry = (HashMap<String, String>) Common.parseJsonKV(subEntryJSON);
                        kvEntry.put("lessonDay", entriesKeys.get(i));
                        entries.add(kvEntry);
                    }

                }
                timetable.setEntries(entries);
            }catch (JSONException ignored){}

            calendar.setTimetable(timetable);
            publishProgress("100%");
            return calendar;
            }catch(JSONException e){
                e.printStackTrace();
            }

        return null;
        }

    @Override
    protected void onPostExecute(UCCalendar calendar) {
        super.onPostExecute(calendar);
        caller.onTaskComplete(this);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }
}
