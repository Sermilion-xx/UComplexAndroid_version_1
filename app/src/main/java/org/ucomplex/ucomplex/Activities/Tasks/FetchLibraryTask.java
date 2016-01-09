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
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, String> httpParams = new HashMap<>();
        if(params[0]==0){
            //collections  collections (0) - > all_selections (1) - > open_selection (selection - 45, pure - 1) (2)
            //категории
            urlString = "https://chgu.org/user/collections?mobile=1";
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
            if(jsonData!=null){
                if(jsonData.length()>0) {
                    return getCollectionDataFromJson(jsonData);
                }
            }

        }else if(params[0]==1){
            //all selections
            //просто категории
            urlString = "https://chgu.org/user/collections/all_sections?mobile=1";
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
            if(jsonData!=null){
                if(jsonData.length()>0) {
                    return getSectionDataFromJson(jsonData);
                }
            }

        }else if(params[0]==2){
            //single selection
            urlString = "https://chgu.org/user/collections/open_section?mobile=1";
            httpParams.put("pure", "1");
            httpParams.put("collections_sections",String.valueOf(params[1]));
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), httpParams);
            if(jsonData!=null){
                if(jsonData.length()>0) {
                    return getBookDataFromJson(jsonData);
                }
            }

        }else if(params[0]==3){
            //all books for category
            urlString = "https://chgu.org/user/collections/open_collection?mobile=1";
            httpParams.put("collection",String.valueOf(params[1]));
            jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), httpParams);
            if(jsonData!=null){
                if(jsonData.length()>0) {
                    return getSectionDataFromJson(jsonData);
                }
            }

        }



        //collections - > open_collection (colection - 10) - > open_selection (selection - 45, pure - 1)

        return null;
    }

    private ArrayList getBookDataFromJson(String jsonData) {
        libraryData = new ArrayList<>();
        try{
            JSONObject booksJsonObject = new JSONObject(jsonData);
            JSONObject booksJson = booksJsonObject.getJSONObject("books");
            JSONArray bookJson = booksJson.getJSONArray("book");

            for(int i=0;i<bookJson.length();i++){
                JSONObject jsonBookObject =  bookJson.getJSONObject(i);
                int id = jsonBookObject.getInt("id");
                String name = jsonBookObject.getString("name");
                int quantity = jsonBookObject.getInt("quantity");
                int year = jsonBookObject.getInt("year");
                int authorInt = -1;
                String authorStr = "";

                JSONObject authorsJson = booksJson.getJSONObject("authors");
                JSONObject bookAuthorJson = authorsJson.getJSONObject("books_authors");
                JSONArray bookAuthorArray;
                ArrayList<String> keys = Common.getKeys(bookAuthorJson);
                for(int l=0;l<keys.size();l++){
                    bookAuthorArray = bookAuthorJson.getJSONArray(String.valueOf(keys.get(l)));
                    for(int j=0;j<bookAuthorArray.length();j++){
                        int bookId = bookAuthorArray.getJSONObject(j).getInt("book");
                        if(bookId == id){
                            authorInt = bookId;
                            JSONObject authorsObjectJson = authorsJson.getJSONObject("authors");
                            ArrayList<String> keys1 = Common.getKeys(authorsObjectJson);
                            for(int k=0;k<keys1.size();k++){
                                JSONObject authorObject = authorsObjectJson.getJSONObject(String.valueOf(keys1.get(k)));
                                if(authorObject.getInt("id") == authorInt){
                                    authorStr = authorObject.getString("name");
                                }
                            }
                        }
                    }
                }
                Quintet<Integer, String, String, Integer,Integer> book =
                        new Quintet(id, name, authorStr, quantity, year);
                libraryData.add(book);

            }
            return libraryData;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    //id, name, edition, quantity, year - collection
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

    //id, name - section
    private ArrayList<Quintet<Integer, String, String, Integer,Integer>> getSectionDataFromJson(String jsonData){
        libraryData = new ArrayList<>();
        JSONObject selectionJson = null;

        try{
            selectionJson = new JSONObject(jsonData);
            try {
                JSONArray collectionArray = selectionJson.getJSONArray("sections");
                for(int i=0;i<collectionArray.length();i++){
                    JSONObject selectionJsonItemJson = collectionArray.getJSONObject(i);
                    int id = selectionJsonItemJson.getInt("id");
                    String name = selectionJsonItemJson.getString("name");
                    //id, name, -1, -1, -1
                    Quintet<Integer, String, String, Integer,Integer> selectionItem = new Quintet<>(id, name,"-1", -1,-1);
                    libraryData.add(selectionItem);
                }
            }catch (JSONException e){
                //id, collection, section,client, -1
                JSONObject collectionObject = selectionJson.getJSONObject("sections");

                JSONArray collectionsSectionsJSON = collectionObject.getJSONArray("collections_sections");

                JSONObject sectionJson = collectionObject.getJSONObject("sections");

                for(int i=0;i<collectionsSectionsJSON.length();i++){
                    JSONObject collectionSection = collectionsSectionsJSON.getJSONObject(i);
                    String key = collectionSection.getString("section");
                    JSONObject jsonObject = sectionJson.getJSONObject(key);
                    String collection = collectionSection.getString("collection");
                    int client = collectionSection.getInt("client");
                    int id = collectionSection.getInt("id");
                    String name = jsonObject.getString("name");
                    //id, name, collection, client, -1
                    Quintet<Integer, String, String, Integer,Integer> selectionItem = new Quintet<>(id, name, collection, client,-2);
                    libraryData.add(selectionItem);
                }
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
