package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.ucomplex.ucomplex.Activities.MyFilesActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.File;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 11/12/2015.
 */
public class FetchMyFilesTask extends AsyncTask<String, String, ArrayList<File>> implements IProgressTracker, DialogInterface.OnCancelListener {

    Activity mContext;
    MyFilesActivity caller;

    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;

    ArrayList<File> files;

    public FetchMyFilesTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (MyFilesActivity) mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public void setupTask(String ... params) {
        this.setProgressTracker(this);
        this.execute(params);
    }

    @Override
    protected ArrayList<File> doInBackground(String... params) {
        String urlString = "http://you.com.ru/student/my_files?mobile=1";
        HashMap<String, String> httpParams = new HashMap<>();
        String jsonData = "";
        if(params.length>0){
            httpParams.put("folder", params[0]);
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext),httpParams);
        }else{
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        }
        if(jsonData!=null){
            if(jsonData.length()>0){
                return Common.getFileDataFromJson(jsonData,mContext);
            }
        }
        return new ArrayList<>();
    }

    @Override
    protected void onCancelled() {
        mProgressTracker = null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        String mProgressMessage = values[0];
        // And send it to progress tracker
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
        }
    }

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем файлы");
            if (files != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList filesData) {
        super.onPostExecute(filesData);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
            mProgressTracker = null;
        }
    }

    @Override
    public void onProgress(String message) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
    }
}
