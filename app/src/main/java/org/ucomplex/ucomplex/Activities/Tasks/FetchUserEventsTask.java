package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.EventParams;
import org.ucomplex.ucomplex.Model.EventRowItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class FetchUserEventsTask extends AsyncTask<Integer, Void, ArrayList<EventRowItem>> implements IProgressTracker, DialogInterface.OnCancelListener{

    Activity mContext;
    String jsonData  = null;
    private OnTaskCompleteListener mTaskCompleteListener = null;

    public FetchUserEventsTask(Activity _context, OnTaskCompleteListener ... taskCompleteListener) {
        mContext = _context;
        if(taskCompleteListener.length>0){
            this.mTaskCompleteListener = taskCompleteListener[0];
        }
    }


    @Override
    protected ArrayList<EventRowItem> doInBackground(Integer... params) {
        String urlString = "";
        if(params[0]==4){
            urlString = "http://you.com.ru/student?mobile=1";
        }else if(params[0]==3){
            urlString = "http://you.com.ru/teacher?mobile=1";
        }
        if(params.length>1){
            urlString = "http://you.com.ru/user/events?mobile=1";
            HashMap<String, String> httpParams = new HashMap<>();
            httpParams.put("start", String.valueOf(params[1]));
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext),httpParams);
        }else{
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        }
        try {
            if(jsonData!=null){
                if(jsonData.length()>0) {
                    return getEventsDataFromJson(this.jsonData);
                }
            }
        } catch (JSONException e) {
        e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(final ArrayList<EventRowItem> items) {

        if(mTaskCompleteListener!=null){
            mTaskCompleteListener.onTaskComplete(this);
        }else {
            if (Common.connection != null) {
                String uc_version = Common.connection.getHeaderField("X-UVERSION");
                if (uc_version != null) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String uc_version_pref = prefs.getString("X-UVERSION", "");
                    if (!uc_version.equals(uc_version_pref)) {
                        FetchLangTask flt = new FetchLangTask();
                        flt.setmContext(mContext);
                        boolean success = false;
                        try {
                            success = flt.execute().get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (success) {
                            Common.X_UVERSION = uc_version;
                            prefs.edit().putString("X-UVERSION", uc_version).apply();
                        }
                    }
                }
            }
        }
    }


    private ArrayList<EventRowItem> getEventsDataFromJson(String forecastJsonStr)
            throws JSONException {
        ArrayList<EventRowItem> displayEventsArray = new ArrayList<>();

        JSONObject eventJson = new JSONObject(forecastJsonStr);
        JSONArray eventsArray = eventJson.getJSONArray("events");

        for(int i = 0; i < eventsArray.length(); i++) {
            EventRowItem item = new EventRowItem();
            EventParams params = item.getParams();
            JSONObject event = eventsArray.getJSONObject(i);
            int type = Integer.parseInt(event.getString("type"));
            JSONObject paramsJson = new JSONObject(event.getString("params"));
            String displayEvent = makeEvent(type, paramsJson);
            String time = Common.makeDate(event.getString("time"));

            if(type!=6){
                if(paramsJson.has("type")){
                    params.setType(paramsJson.getInt("type"));
                }
                if(paramsJson.has("id")) {
                    params.setId(paramsJson.getInt("id"));
                }
                if(paramsJson.has("name")) {
                    params.setName(paramsJson.getString("name"));
                }
                if(paramsJson.has("photo")) {
                    params.setPhoto(paramsJson.getInt("photo"));
                }
                if(paramsJson.has("gcourse")) {
                    params.setGcourse(paramsJson.getInt("gcourse"));
                }
                if(paramsJson.has("courseName")) {
                    params.setCourseName(paramsJson.getString("courseName"));
                }
                if(paramsJson.has("hourType")) {
                    params.setHourType(paramsJson.getInt("hourType"));
                }
                if(paramsJson.has("code")) {
                    params.setCode(paramsJson.getString("code"));
                }
            }else{
                try{
                    int param_type = paramsJson.getInt("type");
                    params.setType(param_type);
                }catch (JSONException ignored){}
            }
            item.setParams(params);
            item.setEventText(displayEvent);
            item.setTime(time);
            item.setSeen(Integer.parseInt(event.getString("seen")));
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


    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onProgress(String message) {

    }

    @Override
    public void onComplete() {

    }
}