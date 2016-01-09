package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.javatuples.Triplet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.SubjectsActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 10/12/2015.
 */
public class FetchSubjectsTask extends AsyncTask<Void, String, ArrayList<Triplet<String, String, Integer>>> implements IProgressTracker, DialogInterface.OnCancelListener{

    Activity mContext;
    String[] assesmentType = {"Зачет","Экзамен", "Самостоятельная работа"};
    SubjectsActivity caller;

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;
    ArrayList<Triplet<String, String, Integer>> subjectsListArray;


    public FetchSubjectsTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (SubjectsActivity) mContext;
        this.mTaskCompleteListener = taskCompleteListener;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
    }

    public void setupTask(Void ... params) {
        this.setProgressTracker(this);
        this.execute(params);
    }

    @Override
    protected ArrayList<Triplet<String, String, Integer>> doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student/subjects_list?json";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));

        if(jsonData!=null){
            if(jsonData.length()>0){
                return getSubjectDataFromJson(jsonData);
            }
        }
        return null;
    }

    @Nullable
    private ArrayList<Triplet<String, String, Integer>> getSubjectDataFromJson(String jsonData){
        subjectsListArray = new ArrayList<>();
        JSONObject subjectsJson = null;
        try {
            subjectsJson = new JSONObject(jsonData);
            JSONObject courses = subjectsJson.getJSONObject("courses");
            JSONObject coursesForms = subjectsJson.getJSONObject("courses_forms");
            JSONArray studentSubjectsList = subjectsJson.getJSONArray("studentSubjectsList");
            HashMap<String, String> hashCourses = (HashMap<String, String>) Common.parseJsonKV(courses);
            HashMap<String, String> hashCoursesForms = (HashMap<String, String>) Common.parseJsonKV(coursesForms);

            ArrayList<HashMap<String, String>> studentSubjectsListHashMap = new ArrayList<>();
            for(int i =0;i<studentSubjectsList.length();i++){
                HashMap<String, String> hashSubj = (HashMap<String, String>) Common.parseJsonKV(studentSubjectsList.getJSONObject(i));
                studentSubjectsListHashMap.add(hashSubj);
            }
            for(int i = 0; i<studentSubjectsListHashMap.size();i++){
                int    gcourse = Integer.parseInt(studentSubjectsListHashMap.get(i).get("id"));
                String _courseNameId = studentSubjectsListHashMap.get(i).get("course");
                String courseName = hashCourses.get(_courseNameId);
                int courseFrom = Integer.parseInt(hashCoursesForms.get(_courseNameId));
                Triplet<String, String, Integer> subject = new Triplet<>(courseName, assesmentType[courseFrom], gcourse);
                subjectsListArray.add(subject);
            }
            return subjectsListArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList fileArrayList) {
        super.onPostExecute(fileArrayList);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }

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

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем дисциплины");
            if (subjectsListArray != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    public void onProgress(String message) {
        // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        // Show current message in progress dialog
        mProgressDialog.setMessage(message);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Cancel task
        this.cancel(true);
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    public void onComplete() {
        // Close progress dialog
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(this);
        mProgressDialog.dismiss();
        // Reset task
    }
}
