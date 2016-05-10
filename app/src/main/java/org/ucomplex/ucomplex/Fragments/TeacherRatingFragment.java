package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherRating;
import org.ucomplex.ucomplex.Adaptors.TeacherRatingAdapter;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.TeacherRating;
import org.ucomplex.ucomplex.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 30/04/16.
 */
public class TeacherRatingFragment extends ListFragment implements OnTaskCompleteListener {

    private TeacherRating teacherRating;
    LinearLayout linlaHeaderProgress;

    private int teacher;
    private Activity mContext;
    private boolean myTeacher = false;
    private TeacherRatingAdapter adapter;

    public TeacherRatingFragment() {

    }

    @Override
    public TeacherRatingAdapter getListAdapter() {
        return adapter;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public void setMyTeacher(boolean myTeacher) {
        this.myTeacher = myTeacher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchTeacherRating fetchTeacherRating = new FetchTeacherRating(mContext, this);
        fetchTeacherRating.execute(String.valueOf(teacher));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(mContext, "Загрузка отменена", Toast.LENGTH_LONG).show();
        } else {
            try {
                teacherRating = (TeacherRating) task.get();
                adapter = new TeacherRatingAdapter(mContext, teacherRating);
                setListAdapter(adapter);
                System.out.println();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
