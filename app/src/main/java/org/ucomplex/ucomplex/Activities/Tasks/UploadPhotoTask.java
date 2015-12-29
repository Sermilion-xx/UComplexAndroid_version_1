package org.ucomplex.ucomplex.Activities.Tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ucomplex.ucomplex.MyServices;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Sermilion on 29/12/2015.
 */
public class UploadPhotoTask extends AsyncTask<Object, Void, Integer> implements IProgressTracker, DialogInterface.OnCancelListener{

    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;
    private int responseCode = -1;
    private Context context;

    public UploadPhotoTask(Context context, OnTaskCompleteListener taskCompleteListener){
        this.mTaskCompleteListener = taskCompleteListener;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
        this.context = context;
    }

    public void setupTask(Object ... params) {
        this.setProgressTracker(this);
        this.execute(params);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected Integer doInBackground(Object... params) {
        HttpPost httpPost = new HttpPost("http://you.com.ru/user/load_photo");
        try {
            MultipartEntity entity = new MultipartEntity();
            ByteArrayBody fileBody = (ByteArrayBody) params[0];
            entity.addPart("photo", fileBody);
            httpPost.setEntity(entity);

            String auth = MyServices.getLoginDataFromPref(context);
            final byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            final String encoded = Base64.encodeToString(authBytes, flags);

            httpPost.setHeader("Authorization", "Basic "+encoded);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            response.getEntity().consumeContent();
            StatusLine statusLine = response.getStatusLine();
            responseCode = statusLine.getStatusCode();
            return responseCode;
        } catch (Exception e) {
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

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем фото");
            if (responseCode != -1) {
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
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
        mProgressDialog.dismiss();
    }


    @Override
    protected void onPostExecute(Integer responseCode) {
        super.onPostExecute(responseCode);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }

}