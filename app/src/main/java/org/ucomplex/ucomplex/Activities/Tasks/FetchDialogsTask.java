package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.MessagesListActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Dialog;

import java.util.ArrayList;

/**
 * Created by Sermilion on 30/12/2015.
 */
public class FetchDialogsTask extends AsyncTask<Integer, String, ArrayList> implements IProgressTracker, DialogInterface.OnCancelListener {

    Activity mContext;
    MessagesListActivity caller;
    ArrayList<Dialog> dialogsList;

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;

    public FetchDialogsTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (MessagesListActivity) mContext;
        this.mTaskCompleteListener = taskCompleteListener;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
    }


    public void setupTask(Integer ... params) {
        this.setProgressTracker(this);
        this.execute(params);
    }

    @Override
    protected ArrayList doInBackground(Integer... params) {
        String urlString = "https://chgu.org/user/messages?mobile=1";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        if(jsonData!=null){
            if(jsonData.length()>0) {
                return getDialogsFromJson(jsonData);
            }
        }
        return null;
    }

    private ArrayList getDialogsFromJson(String jsonData) {
        dialogsList = new ArrayList<>();
        try{
            JSONArray dialogsJson = new JSONObject(jsonData).getJSONArray("dialogs");
            for(int i=0;i<dialogsJson.length();i++){
                JSONObject dialogJson = dialogsJson.getJSONObject(i);
                Dialog dialog = new Dialog();
                dialog.setCode(dialogJson.getString("code"));
                dialog.setId(dialogJson.getInt("from"));
                dialog.setCompanion(dialogJson.getInt("companion"));
                dialog.setFrom(dialogJson.getInt("from"));
                dialog.setMessage(dialogJson.getString("message"));
                dialog.setName(dialogJson.getString("name"));
                dialog.setPhoto(dialogJson.getInt("photo"));
                dialog.setStatus(dialogJson.getInt("status"));
                dialog.setTime(dialogJson.getString("time"));
                dialogsList.add(dialog);
            }
            publishProgress("100%");
            return dialogsList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        super.onPostExecute(arrayList);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем диалоги");
            if (dialogsList != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    public void onProgress(String message) {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        mProgressDialog.setMessage(message);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
        mProgressDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }
}
