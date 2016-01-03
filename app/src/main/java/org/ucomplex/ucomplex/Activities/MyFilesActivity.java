package org.ucomplex.ucomplex.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyFilesActivity extends AppCompatActivity implements OnTaskCompleteListener {

    private ArrayList<File> mItems;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(this,this);
        fetchMyFilesTask.setupTask();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                    mItems = (ArrayList) task.get();
                    CourseMaterialsFragment courseMaterialsFragment = new CourseMaterialsFragment();
                    courseMaterialsFragment.setFiles(mItems);
                    courseMaterialsFragment.setMyFiles(true);
                    courseMaterialsFragment.setmContext(MyFilesActivity.this);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_my_files, courseMaterialsFragment);
                    if(!first)
                        fragmentTransaction.addToBackStack("Мои файлы");
                    else
                        first = false;
                    fragmentTransaction.commit();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
