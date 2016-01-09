package org.ucomplex.ucomplex.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import org.ucomplex.ucomplex.Activities.Tasks.FetchDialogsTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.MessagesListAdapter;
import org.ucomplex.ucomplex.Model.Dialog;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MessagesListActivity extends AppCompatActivity implements OnTaskCompleteListener {

    ArrayList<Dialog> dialogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Сообщения");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FetchDialogsTask fetchDialogsTask = new FetchDialogsTask(this, this);
        fetchDialogsTask.setupTask();
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

            Toast.makeText(this, "Операция была прервана", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                dialogs = (ArrayList<Dialog>) task.get();
                if(dialogs!=null && dialogs.size()>0){
                    MessagesListAdapter messagesListAdapter = new MessagesListAdapter(this,dialogs);
                    ListView listView = (ListView) findViewById(R.id.messages_listview);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MessagesListActivity.this, MessagesActivity.class);
                            intent.putExtra("companion", String.valueOf(dialogs.get(position).getCompanion()));
                            startActivity(intent);
                        }
                    });
                    listView.setAdapter(messagesListAdapter);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
