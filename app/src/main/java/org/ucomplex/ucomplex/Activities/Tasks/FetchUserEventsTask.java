package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.EventParams;
import org.ucomplex.ucomplex.Model.EventRowItem;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class FetchUserEventsTask extends AsyncTask<Void, Void, ArrayList<EventRowItem>> {

    Activity mContext;
    String jsonData  = null;

    public FetchUserEventsTask(Activity _context) {
        mContext = _context;
    }

    public FetchUserEventsTask(){

    }

    @Override
    protected ArrayList<EventRowItem> doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student?json";
//        String authParams = this.user.getLogin()+":"+this.user.getPass()+":"+this.user.getRoles().get(0).getId();
        jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        try {
            return getEventsDataFromJson(this.jsonData);
        } catch (JSONException e) {
        e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final ArrayList<EventRowItem> items) {
        String uc_version = MyServices.connection.getHeaderField("X-UVERSION");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String uc_version_pref = prefs.getString("X-UVERSION", "");

        if(!uc_version.equals(uc_version_pref)){
            FetchLangTask flt = new FetchLangTask();
            flt.setmContext(mContext);
//            flt.setParams(student.getLogin()+":"+student.getPass()+":"+student.getRoles().get(0).getId());
            boolean success = false;
            try {
                success = flt.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if(success){
                MyServices.X_UVERSION = uc_version;
                prefs.edit().putString("X-UVERSION",uc_version).apply();
            }
        }
    }


    private ArrayList<EventRowItem> getEventsDataFromJson(String forecastJsonStr)
            throws JSONException {
        ArrayList<EventRowItem> displayEventsArray = new ArrayList<>();
        // These are the names of the JSON objects that need to be extracted.
        final String JSON_EVENTS_ID = "id";
        final String JSON_EVENTS = "events";
        final String JSON_EVENTS_PARAMS = "params";
        final String JSON_EVENTS_PARAMS_ID = "id";
        final String JSON_EVENTS_PARAMS_NAME = "name";
        final String JSON_EVENTS_PARAMS_PHOTO = "photo";
        final String JSON_EVENTS_PARAMS_CODE = "code";
        final String JSON_EVENTS_PARAMS_GCOURSE = "gcourse";
        final String JSON_EVENTS_PARAMS_COURSENAME = "courseName";
        final String JSON_EVENTS_PARAMS_HOURTYPE = "hourType";
        final String JSON_EVENTS_PARAM_TYPE = "type";
        final String JSON_EVENTS_TYPE = "type";
        final String JSON_EVENTS_TIME = "time";
        final String JSON_EVENTS_SEEN = "seen";


        JSONObject eventJson = new JSONObject(forecastJsonStr);
        JSONArray eventsArray = eventJson.getJSONArray(JSON_EVENTS);

        for(int i = 0; i < eventsArray.length(); i++) {
            EventRowItem item = new EventRowItem();
            EventParams params = item.getParams();
            JSONObject event = eventsArray.getJSONObject(i);
            int type = Integer.parseInt(event.getString(JSON_EVENTS_TYPE));
            JSONObject paramsJson = new JSONObject(event.getString(JSON_EVENTS_PARAMS));
            String displayEvent = makeEvent(type, paramsJson);
            String time = Common.makeDate(event.getString(JSON_EVENTS_TIME));

            if(type!=6){
                int param_id = paramsJson.getInt(JSON_EVENTS_PARAMS_ID);
                int param_photo = paramsJson.getInt(JSON_EVENTS_PARAMS_PHOTO);
                String param_code = paramsJson.getString(JSON_EVENTS_PARAMS_CODE);
                int param_gcourse = paramsJson.getInt(JSON_EVENTS_PARAMS_GCOURSE);
                String param_courseName = paramsJson.getString(JSON_EVENTS_PARAMS_COURSENAME);
                int param_hourType = paramsJson.getInt(JSON_EVENTS_PARAMS_HOURTYPE);
                String param_name = paramsJson.getString(JSON_EVENTS_PARAMS_NAME);

                params.setId(param_id);
                params.setName(param_name);
                params.setPhoto(param_photo);
                params.setGcourse(param_gcourse);
                params.setCourseName(param_courseName);
                params.setHourType(param_hourType);
                params.setCode(param_code);
            }else{
                int param_type = paramsJson.getInt(JSON_EVENTS_PARAM_TYPE);
                params.setType(param_type);
            }
            item.setEventText(displayEvent);
            item.setTime(time);
            item.setType(type);
            displayEventsArray.add(item);
        }

        return displayEventsArray;
    }

    private String makeEvent(int type, JSONObject params)
            throws NumberFormatException, JSONException {
        String result = "";
        String[] hourTypes = new String[] { "протокол занятия",
                "протокол рубежного контроля", "экзаменационную ведомость",
                "индивидуальное занятие" };
        String courseName;
        String name;
        switch (type) {

            case 1:
                courseName = params.getString("courseName");
                result = "Загружен материал по дисциплине " + courseName + ".";
                break;

            case 2:
                int hourType = Integer.parseInt(params.getString("hourType"));
                courseName = params.getString("courseName");
                name = params.getString("name");
                result = "Преподаватель " + name + " заполнил "
                        + hourTypes[hourType] + " по дисциплине " + courseName
                        + ".";
                break;

            case 3:
                String semestr = params.getString("semester");
                String year = params.getString("year");
                result = "Вы произвели оплату за " + semestr + "-й семестр " + year
                        + " учебного года.";
                break;

            case 4:
                String eventName = params.getString("eventName");
                String date = params.getString("date");
                result = "Вы приглашены на участие в мероприятии " + eventName
                        + ", которое состоится " + date;
                break;

            case 5:
                name = params.getString("name");
                String author = params.getString("author");
                result = "В книжную полку добавлена книга " + name + ", " + author;
                break;

            case 6:
                String message = "";
                if (params.has("type")) {
                    String t = params.getString("type");
                    if (t.equals("1")) {
                        message = "Ваша фотография отклонена из-за несоответствия условиям загрузки личной фотографии.";
                    }
                } else {
                    message = params.getString("message");
                }
                result = "СИСТЕМНОЕ СООБЩЕНИЕ: \n" + message;
                break;
        }
        return result;
    }



}