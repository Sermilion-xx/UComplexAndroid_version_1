package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.TeacherRating;
import org.ucomplex.ucomplex.Model.Votes;

import java.util.ArrayList;

/**
 * Created by Sermilion on 22/03/16.
 */
public class FetchTeacherRating extends AsyncTask<Void, TeacherRating, TeacherRating> implements IProgressTracker, DialogInterface.OnCancelListener{

    Activity mContext;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;

    public FetchTeacherRating(Activity mContext, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    @Override
    protected TeacherRating doInBackground(Void... params) {
        String urlString = "http://you.com.ru/user/page/21866?mobile=1";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        if(jsonData!=null){
            return getRatingDataFromJson(jsonData);
        }
        return new TeacherRating();
    }

    private TeacherRating getRatingDataFromJson(String jsonData) {
        JSONObject teacherRatingJson;
        TeacherRating teacherRating = new TeacherRating();
        try {
            teacherRatingJson = new JSONObject(jsonData);
            int teacher = teacherRatingJson.getInt("teacher");
            boolean myTeacher = teacherRatingJson.getBoolean("my_teacher");
            JSONObject questionsJson = teacherRatingJson.getJSONObject("votes");
            teacherRating.setMy_teacher(myTeacher);
            teacherRating.setTeacher(teacher);
            for(int i=0; i<10; i++){
                JSONObject question = questionsJson.getJSONObject(String.valueOf(i));
                ArrayList<String> keys = Common.getKeys(question);
                Votes votes = new Votes();
                for(int j = 0; j<keys.size(); j++){
                    try{
                        votes.setOne(question.getInt(keys.get(i)));
                    }catch (JSONException e){
                        e.printStackTrace();
                        votes.setOne(0);
                    }
                }
                teacherRating.addVote(votes);
            }
            return teacherRating;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onProgress(String message) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
