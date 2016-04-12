package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.CourseActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 02/01/2016.
 */
public class FetchTeacherFilesTask extends AsyncTask<Object, String, ArrayList> implements  DialogInterface.OnCancelListener, IProgressTracker {

    Activity mContext;
    CourseActivity caller;
    ArrayList<File> fileArrayList;
    User owner;

    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;

    public FetchTeacherFilesTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (CourseActivity) mContext;

        this.mTaskCompleteListener = taskCompleteListener;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setupTask(String ... params) {
//        this.setProgressTracker(this);
        this.execute(params);
    }

    @Override
    protected ArrayList doInBackground(Object... params) {
        String urlString = "https://ucomplex.org/student/ajax/teacher_files?mobile=1";
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("folder", (String) params[0]);
        if(params.length>1){
            this.owner = (User) params[1];
        }
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext),httpParams);
        fileArrayList = getFileDataFromJson(jsonData);
        return fileArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList fileArrayList) {
        super.onPostExecute(fileArrayList);
        mTaskCompleteListener.onTaskComplete(this);
    }

    private ArrayList<File> getFileDataFromJson(String jsonData) {
        fileArrayList = new ArrayList<>();
        JSONObject filesJson = null;

        try{
            filesJson = new JSONObject(jsonData);
            JSONArray filesArray = filesJson.getJSONArray("files");
            for(int i=0;i<filesArray.length();i++){
                JSONObject fileItemJson = filesArray.getJSONObject(i);
                File file = new File();
                file.setName(fileItemJson.getString("name"));
                file.setTime(fileItemJson.getString("time"));
                file.setAddress(fileItemJson.getString("address"));
                file.setData(fileItemJson.getString("data"));
                file.setId(fileItemJson.getString("id"));
                file.setSize(fileItemJson.getInt("size"));
                file.setOwner(owner);
                file.setType(fileItemJson.getString("type"));
                fileArrayList.add(file);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fileArrayList;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    public void onProgress(String message) {

    }

    @Override
    public void onComplete() {

    }
}
