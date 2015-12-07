package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.EventRowItem;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;

/**
 * Created by Sermilion on 07/12/2015.
 */
public class FetchCalendarBeltTask extends AsyncTask<Void, Void, ArrayList<EventRowItem>> {

    Activity mContext;
    String jsonData  = null;


    @Override
    protected ArrayList<EventRowItem> doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student/ajax/calendar_belt?json";
        jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return getCalendarBeltDataFromJson(this.jsonData);

    }

    private ArrayList<EventRowItem> getCalendarBeltDataFromJson(String jsonData) {

        return null;
    }
}
