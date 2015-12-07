package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.MyServices;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class FetchLangTask extends AsyncTask<Void, Void, Boolean> {

    Activity mContext;
    String jsonData;

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String urlString = "http://you.com.ru/public/get_uc_vars?json&lang=1";
        jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        PrintWriter out = null;
        try {
            out = new PrintWriter("lang.txt");
            out.write("");
            out.print(jsonData);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jsonData != null;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {

        }
    }
}