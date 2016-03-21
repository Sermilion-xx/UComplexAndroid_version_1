package org.ucomplex.ucomplex.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.javatuples.Triplet;

import org.ucomplex.ucomplex.Activities.Tasks.FetchSubjectsTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.SubjectsAdapter;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SubjectsActivity extends AppCompatActivity implements OnTaskCompleteListener {

    //course name, assesment type, course id
    private ArrayList<Triplet<String, String, Integer>> mItems;
    ListView listView;
    LinearLayout linlaHeaderProgress;

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_subjects);
        toolbar.setTitle("Дисциплины");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        listView = (ListView) findViewById(R.id.subject_listview);
        FetchSubjectsTask fetchSubjectsTask = new FetchSubjectsTask(this, this);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        fetchSubjectsTask.setupTask();
}

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            // Report about cancel
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                mItems = (ArrayList<Triplet<String, String, Integer>>) task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if(mItems!= null && mItems.size()>0){

                SubjectsAdapter subjectsAdapter = new SubjectsAdapter(this,mItems);
                listView.setAdapter(subjectsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> a, View v,int position, long id)
                    {
                        if(Common.isNetworkConnected(SubjectsActivity.this)){
                            Intent intent = new Intent(getBaseContext(), CourseActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt("gcourse", mItems.get(position).getValue2());
                            extras.putString("courseName", mItems.get(position).getValue0());
                            intent.putExtras(extras);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SubjectsActivity.this, "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_LONG)
                        .show();
            }

        }
    }
}
