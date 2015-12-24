package org.ucomplex.ucomplex.Activities.Tasks;

/**
 * Created by Sermilion on 24/12/2015.
 */
public interface IProgressTracker {
    void onProgress(String message);
    void onComplete();
}
