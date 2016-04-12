package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.TimetableEntry;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;

/**
 * Created by Sermilion on 22/03/16.
 */
public class FetchRolePersonTask extends AsyncTask<Void, User, User> implements IProgressTracker, DialogInterface.OnCancelListener{

    Activity mContext;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;

    public FetchRolePersonTask(Activity mContext, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    @Override
    protected User doInBackground(Void... params) {
        String urlString = "https://ucomplex.org/user/page/21866?mobile=1";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        if(jsonData!=null){
            return getUserDataFromJson(jsonData);
        }
        return new User();
    }

    @Nullable
    private User getUserDataFromJson(String jsonData){
        User user;
        JSONObject userJson;
        try{
            userJson = new JSONObject(jsonData);
            user = new User();
            user.setId(userJson.getInt("id"));
            user.setName(userJson.getString("name"));
            user.setType(userJson.getInt("type"));
            user.setClosed(userJson.getInt("closed"));
            user.setOnline(userJson.getInt("online"));
            user.setDepartmentId(userJson.getInt("department"));
            user.setEmail(userJson.getString("email"));
            user.setUpqualification(userJson.getString("upqualifications"));
            user.setRank(userJson.getInt("rank"));

            String courses = userJson.getString("courses");
            String[] coursesArray = courses.split("\\r\\n");
            ArrayList<String> coursesList = new ArrayList<>();
            for(String s:coursesArray){
                coursesList.add(s);
            }
            user.setCourses(coursesList);
            user.setBio(userJson.getString("bio"));
            user.setPlan(userJson.getInt("plan"));
            user.setFact(userJson.getInt("fact"));
            user.setActivity(userJson.getInt("activity"));
            user.setDepartmentName(userJson.getString("departmentName"));
            user.setFacultyName(userJson.getString("faculty_name"));
            user.setCode(userJson.getString("code"));
            user.setPhoto(userJson.getInt("photo"));
            user.setCode(userJson.getString("code"));

            try{
                JSONArray timetableJson = userJson.getJSONArray("timetable_courses");
                ArrayList<TimetableEntry> timetableEntryArrayList = new ArrayList<>();
                for(int i = 0; i< timetableJson.length(); i++){
                    JSONObject timetableItem = timetableJson.getJSONObject(i);
                    TimetableEntry timetableEntry = new TimetableEntry();
                    timetableEntry.setId(timetableItem.getInt("id"));
                    timetableEntry.setName(timetableItem.getString("name"));
                    timetableEntry.setDepartment(timetableItem.getString("department"));
                    timetableEntry.setType(timetableItem.getInt("type"));
                    timetableEntry.setCat(timetableItem.getInt("cat"));
                    timetableEntry.setClient(timetableItem.getInt("client"));
                    timetableEntryArrayList.add(timetableEntry);
                }
                user.setTimetableEntries(timetableEntryArrayList);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        onComplete();
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        mProgressTracker = null;
    }


    @Override
    public void onProgress(String message) {
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    protected void onCancelled() {
        mProgressTracker = null;
    }
}
