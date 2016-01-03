package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.javatuples.Quintet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.LibraryActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;

/**
 * Created by Sermilion on 26/12/2015.
 */
public class FetchLibraryTask extends AsyncTask<Integer, String, ArrayList> implements IProgressTracker, DialogInterface.OnCancelListener {

    Activity mContext;
    LibraryActivity caller;

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;

    ArrayList libraryData;

    public FetchLibraryTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (LibraryActivity) mContext;

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
        String urlString = "";
        String jsonData = "";
        if(params[0]==0){
            //collections
            //категории
            urlString = "https://chgu.org/user/collections?json";
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return getCollectionDataFromJson(jsonData);
        }else if(params[0]==1){
            //all selections
            //просто все книги
            urlString = "https://chgu.org/user/collections/all_sections?json";
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return getSectionDataFromJson(jsonData);
        }else if(params[0]==2){
            //single selection
            //all books for category
            urlString = "https://chgu.org/user/collections/open_section";
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return null;
        }

        return null;
    }

    private ArrayList<Quintet> getCollectionDataFromJson(String jsonData){
        libraryData = new ArrayList<>();
        Quintet<Integer, String, String, Integer,Integer> collectionItem1 = new Quintet<>(-1, "Все книги","-1", -1,-1);
        libraryData.add(collectionItem1);
        JSONObject collectionJson = null;

        try{
            collectionJson = new JSONObject(jsonData);
            JSONArray collectionArray = collectionJson.getJSONArray("collections");
            for(int i=0;i<collectionArray.length();i++){
                JSONObject collectionItemJson = collectionArray.getJSONObject(i);
                int type = collectionItemJson.getInt("type");
                int id = collectionItemJson.getInt("id");
                int price = collectionItemJson.getInt("price");
                String name = collectionItemJson.getString("name");
                //id, name, price, type, -1
                Quintet<Integer, String, String, Integer,Integer> collectionItem = new Quintet<>(id, name,String.valueOf(price), type,-1);
                libraryData.add(collectionItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publishProgress("100%");
        return libraryData;
    }

    private ArrayList<Quintet<Integer, String, String, Integer,Integer>> getSectionDataFromJson(String jsonData){
        libraryData = new ArrayList<>();
        JSONObject selectionJson = null;

        try{
            selectionJson = new JSONObject(jsonData);
            JSONArray collectionArray = selectionJson.getJSONArray("sections");
            for(int i=0;i<collectionArray.length();i++){
                JSONObject selectionJsonItemJson = collectionArray.getJSONObject(i);
                int id = selectionJsonItemJson.getInt("id");
                String name = selectionJsonItemJson.getString("name");
                //id, name, -1, -1, -1
                Quintet<Integer, String, String, Integer,Integer> selectionItem = new Quintet<>(id, name,"-1", -1,-1);
                libraryData.add(selectionItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publishProgress("100%");
        return libraryData;
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
            mProgressTracker.onProgress("Загружаем календарь");
            if (libraryData != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList libraryData) {
        super.onPostExecute(libraryData);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
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
