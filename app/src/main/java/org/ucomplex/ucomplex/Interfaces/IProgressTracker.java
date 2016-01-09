package org.ucomplex.ucomplex.Interfaces;

/**
 * Created by Sermilion on 24/12/2015.
 */
public interface IProgressTracker {
    void onProgress(String message);
    void onComplete();
}
