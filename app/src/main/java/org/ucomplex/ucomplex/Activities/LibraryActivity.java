package org.ucomplex.ucomplex.Activities;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchLibraryTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Fragments.LibraryFragment;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LibraryActivity extends AppCompatActivity implements OnTaskCompleteListener {

    ArrayList libraryData;
    boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        toolbar.setTitle("Библиотека");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FetchLibraryTask fetchLibraryTask = new FetchLibraryTask(this,this);
        fetchLibraryTask.setupTask(0);
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
    public void onTaskComplete(AsyncTask task, Object ...data) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG).show();
        } else {
            try {
                libraryData = (ArrayList) task.get();
                //id, name, edition, quantity, year
                int type = 0;
                Quintet<Integer, String, String, Integer, Integer> item = (Quintet<Integer, String, String, Integer, Integer> )libraryData.get(0);
                if(item.getValue4()>1000){
                    type = 1;
                }
                LibraryFragment fragment = new LibraryFragment();
                    fragment.setLibraryData(libraryData);
                    fragment.setType(type);
                fragment.setLibraryActivity(this);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_library, fragment);
                if(!first){
                    fragmentTransaction.addToBackStack(null);
                }
                first=false;
                fragmentTransaction.commit();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }



}
