package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.CalendarActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Calendar.CalendarEvent;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class FetchCalendarTask extends AsyncTask<Void, Void, UCCalendar> {

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
    protected UCCalendar doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student/ajax/calendar?json";
        String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        return getCalendarDataFromJson(jsonData);
    }

    private UCCalendar getCalendarDataFromJson(String jsonData){
        JSONObject calendarJson = null;
        UCCalendar calendar = new UCCalendar();
        try {
            calendarJson = new JSONObject(jsonData);
            calendar.setMethod(calendarJson.getString("method"));
            //Start getting events
            JSONArray eventsJson = calendarJson.getJSONArray("events");

            for (int i = 0; i < eventsJson.length(); i++) {
                JSONObject dayEventsJson = eventsJson.getJSONObject(i);

                Map<String,String> eventMap = new HashMap<>();
                ArrayList<String> keys = new ArrayList<>();
                Iterator iter = dayEventsJson.keys();
                while(iter.hasNext()){
                    String key = (String)iter.next();
                    keys.add(key);
                    String value = dayEventsJson.getString(key);
                    eventMap.put(key,value);
                }

                for(int j=0;j<dayEventsJson.length();j++) {
                    CalendarEvent calendarEvent = new CalendarEvent();
                    JSONObject subEvents = dayEventsJson.getJSONObject(keys.get(j));
                    calendarEvent.setDay(subEvents.getInt("day"));
                    calendarEvent.setDescr(subEvents.getString("descr"));
                    calendarEvent.setHoliday(subEvents.getInt("holiday"));
                    calendarEvent.setStart(subEvents.getString("start"));
                    calendarEvent.setTill(subEvents.getString("till"));
                    calendar.addEvent(calendarEvent);
                }
            }
            //End getting events
            //------------------
            calendar.setAjax(calendarJson.getInt("ajax"));
            calendar.setSubjId(calendarJson.getInt("subjId"));
            calendar.setYear(calendarJson.getString("year"));
            calendar.setMonth(calendarJson.getString("month"));
            calendar.setDay(calendarJson.getString("day"));
            calendar.setPre_month(calendarJson.getString("pre_month"));
            calendar.setNext_month(calendarJson.getString("netx_month"));
            calendar.setGroup(calendarJson.getString("group"));
            calendar.setSubgroup(calendarJson.getString("subgroup"));
            calendar.setCourse(calendarJson.getString("course"));

            //Start getting courses



                return calendar;
            }catch(JSONException e){
                e.printStackTrace();
            }

        return null;
        }

}
