package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.LibraryActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.Model.Library.CollectionItem;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;

/**
 * Created by Sermilion on 26/12/2015.
 */
public class FetchLibraryTask extends AsyncTask<Integer, String, ArrayList> {

    Activity mContext;
    LibraryActivity caller;
    private String mProgressMessage;
    private IProgressTracker mProgressTracker;

    ArrayList libraryData;

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList doInBackground(Integer... params) {
        String urlString = "";
        String jsonData = "";
        if(params[0]==0){
            //collections
            urlString = "https://chgu.org/user/collections?json";
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return getCollectionDataFromJson(jsonData);
        }else if(params[0]==1){
            //all selections
            urlString = "https://chgu.org/user/collections/all_sections?json";
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return getSectionDataFromJson(jsonData);
        }else if(params[0]==2){
            //single selection
            urlString = "https://chgu.org/user/collections/open_section";
            jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
            return null;
        }

        return null;
    }

    private ArrayList<CollectionItem> getCollectionDataFromJson(String jsonData){
        libraryData = new ArrayList<>();
        JSONObject collectionJson = null;

        try{
            collectionJson = new JSONObject(jsonData);
            JSONArray collectionArray = collectionJson.getJSONArray("collections");
            for(int i=0;i<collectionArray.length();i++){
                JSONObject collectionItemJson = collectionArray.getJSONObject(i);
                CollectionItem collectionItem = new CollectionItem();
                collectionItem.setType(collectionItemJson.getInt("type"));
                collectionItem.setId(collectionItemJson.getInt("id"));
                collectionItem.setPrice(collectionItemJson.getInt("price"));
                collectionItem.setName(collectionItemJson.getString("name"));
                libraryData.add(collectionItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publishProgress("100%");
        return libraryData;
    }

    private ArrayList<Pair<Integer, String>> getSectionDataFromJson(String jsonData){
        libraryData = new ArrayList<>();
        JSONObject selectionJson = null;

        try{
            selectionJson = new JSONObject(jsonData);
            JSONArray collectionArray = selectionJson.getJSONArray("sections");
            for(int i=0;i<collectionArray.length();i++){
                JSONObject selectionJsonItemJson = collectionArray.getJSONObject(i);
                int id = selectionJson.getInt("id");
                String name = selectionJson.getString("name");
                Pair<Integer, String> selectionItem = new Pair<>(id, name);
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
        caller.onTaskComplete(this, libraryData);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }
}
