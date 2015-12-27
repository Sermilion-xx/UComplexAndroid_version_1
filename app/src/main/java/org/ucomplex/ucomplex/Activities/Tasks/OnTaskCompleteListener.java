package org.ucomplex.ucomplex.Activities.Tasks;

import android.os.AsyncTask;

/**
 * Created by Sermilion on 24/12/2015.
 */
public interface OnTaskCompleteListener {
    void onTaskComplete(AsyncTask task, Object ... o);
}
