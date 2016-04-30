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
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.Model.Votes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 22/03/16.
 */
public class FetchTeacherRating extends AsyncTask<String, TeacherRating, TeacherRating> implements IProgressTracker, DialogInterface.OnCancelListener{

    Activity mContext;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;

    public FetchTeacherRating(Activity mContext, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    @Override
    protected TeacherRating doInBackground(String... params) {
        String urlString = "https://ucomplex.org/user/get_teacher_votes?json";
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("teacher", params[0]);
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext),postParams);
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

            for(int i=1; i<11; i++){
                try {
                    JSONObject question = questionsJson.getJSONObject(String.valueOf(i));
                    ArrayList<String> keys = Common.getKeys(question);
                    Votes votes = new Votes();
                    for(int j = 0; j<keys.size(); j++){
                        try{
                            votes.setNext(question.getInt(keys.get(j)), Integer.valueOf(keys.get(j))-1);
                        }catch (JSONException e){
                            e.printStackTrace();
                            votes.setOne(0);
                        }
                    }
                    teacherRating.addVote(votes);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            Votes.position = 0;
            return teacherRating;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TeacherRating user) {
        super.onPostExecute(user);
        onComplete();
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        mProgressTracker = null;

    }


    @Override
    public void onProgress(String message) {
//        if (!mProgressDialog.isShowing()) {
//            mProgressDialog.show();
//        }
//        // Show current message in progress dialog
//        mProgressDialog.setMessage(message);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
//        mProgressDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Cancel task
        this.cancel(true);
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        mProgressTracker = null;
    }
}
