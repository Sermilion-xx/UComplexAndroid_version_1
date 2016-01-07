package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.MessagesActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 31/12/2015.
 */
public class FetchMessagesTask extends AsyncTask<String, String, ArrayList> implements DialogInterface.OnCancelListener {

    Activity mContext;
    MessagesActivity caller;
    ArrayList messagesList;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
//    private final ProgressDialog mProgressDialog;

    public FetchMessagesTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (MessagesActivity) mContext;
        this.mTaskCompleteListener = taskCompleteListener;
//        mProgressDialog = new ProgressDialog(context);
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setCancelable(true);
//        mProgressDialog.setOnCancelListener(this);
    }

    public void setupTask(String ... params) {
//        this.setProgressTracker(this);
        this.execute(params);
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        String urlString ="";
        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("companion",params[0]);
        String jsonData = null;
        if(type==0){
            urlString = "http://you.com.ru/user/messages/list?mobile=1";
        }else if(type==1){
            urlString = "https://chgu.org/user/messages/add?mobile=1";
            httpParams.put("msg",params[1]);
            if(params.length>2){
                httpParams.put("file" , params[2]);
            }
        }else if(type==2){
            urlString = "http://you.com.ru/user/messages/list?mobile=1";
            httpParams.put("new","1");
        }
        jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext),httpParams);
        if(type==0){
            return getMessageData(jsonData);
        }else if(type==1){
            return getSentMessageData(jsonData);
        }else if(type==2){
            return getMessageData(jsonData);
        }
        return getMessageData(jsonData);
    }

    private ArrayList getSentMessageData(String jsonData) {
        messagesList = new ArrayList<>();
        Message message = new Message();
        try{
            JSONArray messagesJson = new JSONObject(jsonData).getJSONArray("messages");
            JSONObject messageJson = messagesJson.getJSONObject(0);
            message.setId(messageJson.getInt("id"));
            message.setFrom(messageJson.getInt("from"));
            message.setMessage(messageJson.getString("message"));
            message.setTime(messageJson.getString("time"));
            message.setName(Common.messageCompanionName);
            messagesList.add(message);
            return messagesList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList getMessageData(String jsonData) {
        User user = Common.getUserDataFromPref(mContext);
        int person = user.getPerson();
        String myName = user.getName();
        user = null;
        messagesList = new ArrayList<>();
        try{
            JSONArray messagesJson = new JSONObject(jsonData).getJSONArray("messages");
            JSONObject companionJson = new JSONObject(jsonData).getJSONObject("companion_info");
            Bitmap profileImage = null;
            if(companionJson.getString("photo").equals("1") ) {
                profileImage = Common.getBitmapFromURL(companionJson.getString("code"));
            }
            for(int i=0;i<messagesJson.length();i++){
                JSONObject messageJson = messagesJson.getJSONObject(i);
                Message message = new Message();
                message.setId(messageJson.getInt("id"));
                message.setFrom(messageJson.getInt("from"));
                message.setMessage(messageJson.getString("message"));
                try{
                    message.setStatus(messageJson.getInt("status"));
                }catch (JSONException ignored){}
                message.setTime(messageJson.getString("time"));
                messagesList.add(message);
                if(messageJson.getInt("from")!=person){
                    message.setName(companionJson.getString("name"));
                    if(Common.messageCompanionName.equals("-")){
                        Common.messageCompanionName = companionJson.getString("name");
                    }
                }else{
                    message.setName(myName);
                }
            }
            publishProgress("100%");
            //Последний элемент - фото компаньона
            if(profileImage!=null){
                messagesList.add(profileImage);
            }
            return messagesList;
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        super.onPostExecute(arrayList);
        mTaskCompleteListener.onTaskComplete(this);
    }

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем сообщения");
            if (messagesList != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }
}
