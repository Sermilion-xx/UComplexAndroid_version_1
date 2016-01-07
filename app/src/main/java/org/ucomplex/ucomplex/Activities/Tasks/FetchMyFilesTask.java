package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.MyFilesActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 11/12/2015.
 */
public class FetchMyFilesTask extends AsyncTask<String, String, ArrayList<File>> implements IProgressTracker, DialogInterface.OnCancelListener {

    Activity mContext;
    MyFilesActivity caller;

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;

    ArrayList<File> files;

    public FetchMyFilesTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (MyFilesActivity) mContext;

        this.mTaskCompleteListener = taskCompleteListener;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
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

        return getFileDataFromJson(jsonData);
    }

    private ArrayList<File> getFileDataFromJson(String jsonData){
        files = new ArrayList<>();
        JSONObject fileJson = null;

        try {
            fileJson = new JSONObject(jsonData);
            JSONArray filesArray = fileJson.getJSONArray("files");

            for(int i=0;i<filesArray.length();i++){
                File file = new File();
                JSONObject jsonFile = filesArray.getJSONObject(i);
                file.setSize(jsonFile.getInt("size"));
                file.setTime(jsonFile.getString("time"));
                file.setAddress(jsonFile.getString("address"));
                file.setName(jsonFile.getString("name"));
                file.setType(jsonFile.getString("type"));
                file.setCheckTime(jsonFile.getString("check_time"));
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                Gson gson = new Gson();
                String json = pref.getString("loggedUser", "");
                User obj = gson.fromJson(json, User.class);
                file.setOwner(obj);
                files.add(file);
            }
            return files;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
        }
        mProgressTracker = null;
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
