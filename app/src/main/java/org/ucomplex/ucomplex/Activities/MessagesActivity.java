package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMessagesTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.MessagesAdapter;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MessagesActivity extends AppCompatActivity implements OnTaskCompleteListener {

    ArrayList<Message> messageArrayList = new ArrayList<>();
    ListView listView;
    String companion;
    MessagesAdapter messagesAdapter;



                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_message);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    toolbar.setTitle("Сообщения");
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    companion = getIntent().getStringExtra("companion");
                    listView = (ListView) findViewById(R.id.list_messages_listview);
                    listView.setScrollingCacheEnabled(false);
                    FetchMessagesTask fetchMessagesTask = new FetchMessagesTask(this, this);
                    fetchMessagesTask.setType(0);
                    fetchMessagesTask.setupTask(companion);

                    Button sendMsgButton = (Button) findViewById(R.id.messages_send_button);
                    final TextView messageTextView = (TextView) findViewById(R.id.messages_text);
                    sendMsgButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            FetchMessagesTask sendMessageTask = new FetchMessagesTask(MessagesActivity.this, MessagesActivity.this);
                            sendMessageTask.setType(1);
                            sendMessageTask.setupTask(companion, messageTextView.getText().toString());
                        }
                    });

                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            FetchMessagesTask fetchMessagesTask = new FetchMessagesTask(MessagesActivity.this, MessagesActivity.this);
                            fetchMessagesTask.setType(2);
                            fetchMessagesTask.setupTask(companion);
                        }
                    }, 0, 4000);

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
                FetchMessagesTask fmt = (FetchMessagesTask) task;
                if(fmt.getType()==0){
                    messageArrayList = (ArrayList) task.get();
                    messagesAdapter = new MessagesAdapter(this, messageArrayList);
                    listView.setAdapter(messagesAdapter);
                }else if(fmt.getType()==1 || fmt.getType()==2){
                    ArrayList result = (ArrayList)task.get();
                    if(result!=null && result.size()>0){
                        messageArrayList.add((Message) result.get(0));
                        if(result.size()>0){
                            Toast.makeText(this, "Сообщение отправленно", Toast.LENGTH_LONG)
                                    .show();
                            messagesAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(this, "Ошибка при отправке сообщения", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
