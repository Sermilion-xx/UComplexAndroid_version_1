package org.ucomplex.ucomplex.Activities.Tasks;

/**
 * Created by Sermilion on 24/12/2015.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;

public final class AsyncTaskManager implements IProgressTracker, OnCancelListener {

    private final OnTaskCompleteListener mTaskCompleteListener;
    private FetchCalendarTask mAsyncTask;

    public AsyncTaskManager(Context context, OnTaskCompleteListener taskCompleteListener) {
        // Save reference to complete listener (activity)
        mTaskCompleteListener = taskCompleteListener;
    }

    public void setupTask(FetchCalendarTask asyncTask, String ... params) {
        // Keep task
        mAsyncTask = asyncTask;
        // Wire task to tracker (this)
        mAsyncTask.setProgressTracker(this);
        // Start task
        mAsyncTask.execute(params);
    }

    @Override
    public void onProgress(String message) {
        // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Cancel task
        if(mAsyncTask!=null){
            mAsyncTask.cancel(true);
            // Notify activity about completion
            mTaskCompleteListener.onTaskComplete(mAsyncTask);
            // Reset task
            mAsyncTask = null;
        }
    }

    @Override
    public void onComplete() {
        // Close progress dialog

        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(mAsyncTask);
        // Reset task
        mAsyncTask = null;
    }

    public Object retainTask() {
        // Detach task from tracker (this) before retain
        if (mAsyncTask != null) {
            mAsyncTask.setProgressTracker(null);
        }
        // Retain task
        return mAsyncTask;
    }

    public void handleRetainedTask(Object instance) {
        // Restore retained task and attach it to tracker (this)
        if (instance instanceof FetchCalendarTask) {
            mAsyncTask = (FetchCalendarTask) instance;
            mAsyncTask.setProgressTracker(this);
        }
    }

    public boolean isWorking() {
        // Track current status
        return mAsyncTask != null;
    }
}