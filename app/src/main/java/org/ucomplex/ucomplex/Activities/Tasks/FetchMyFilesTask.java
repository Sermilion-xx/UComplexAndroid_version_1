package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;

/**
 * Created by Sermilion on 11/12/2015.
 */
public class FetchMyFilesTask extends AsyncTask<Void, Void, ArrayList<File>> {

    Activity mContext;

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student/my_files?json";
        String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        return getFileDataFromJson(jsonData);
    }

    private ArrayList<File> getFileDataFromJson(String jsonData){
        ArrayList<File> fileArrayList = new ArrayList<>();
        JSONObject fileJson = null;

        try {
            fileJson = new JSONObject(jsonData);
            JSONArray filesArray = fileJson.getJSONArray("files");

            for(int i=0;i<filesArray.length();i++){
                File file = new File();
                JSONObject jsonFile = filesArray.getJSONObject(i);
                file.setSize(jsonFile.getInt("size"));
                file.setTime(jsonFile.getString("time"));
                file.setName(jsonFile.getString("name"));
                file.setCheckTime(jsonFile.getString("check_time"));
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                Gson gson = new Gson();
                String json = pref.getString("loggedUser", "");
                User obj = gson.fromJson(json, User.class);
                file.setOwner(obj);
                fileArrayList.add(file);
            }
            return fileArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
