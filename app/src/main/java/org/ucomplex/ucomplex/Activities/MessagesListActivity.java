package org.ucomplex.ucomplex.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchDialogsTask;
import org.ucomplex.ucomplex.Adaptors.MessagesListAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Dialog;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MessagesListActivity extends AppCompatActivity implements OnTaskCompleteListener {

    ArrayList<Dialog> dialogs = new ArrayList<>();
    MessagesListAdapter messagesListAdapter;
    private int selectedItemPos;
    LinearLayout linlaHeaderProgress;
    Button newMessageButton;

    @Override
    protected void onResume() {
        super.onResume();
        if (messagesListAdapter != null) {
            messagesListAdapter.notifyDataSetChanged();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("org.ucomplex.newMessageListBroadcast");
        registerReceiver(receiver, filter);
        FetchDialogsTask fetchDialogsTask = new FetchDialogsTask(MessagesListActivity.this, MessagesListActivity.this);
        fetchDialogsTask.setupTask();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        if (messagesListAdapter != null) {
            messagesListAdapter.notifyDataSetChanged();
        }
        super.onPause();

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int messageCount = bundle.getInt("newMessage");
            if (messageCount > 0) {
                FetchDialogsTask fetchDialogsTask = new FetchDialogsTask(MessagesListActivity.this, MessagesListActivity.this);
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                fetchDialogsTask.setupTask();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        newMessageButton = (Button) findViewById(R.id.message_list_new_message_button);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessagesListActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setTitle("Сообщения");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        FetchDialogsTask fetchDialogsTask = new FetchDialogsTask(this, this);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
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

    public void deleteDialog(final int position) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> httpParas = new HashMap<>();
                httpParas.put("id", String.valueOf(dialogs.get(position).getCompanion()));
                String url = "https://ucomplex.org/user/messages/del_dialog?mobile=1";
                String result = Common.httpPost(url, Common.getLoginDataFromPref(MessagesListActivity.this), httpParas);
                return result;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                dialogs.remove(position);
                messagesListAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {

            Toast.makeText(this, "Операция была прервана", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                linlaHeaderProgress.setVisibility(View.GONE);
                dialogs = (ArrayList<Dialog>) task.get();
                messagesListAdapter = new MessagesListAdapter(this, dialogs);
                ListView listView = (ListView) findViewById(R.id.messages_listview);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MessagesListActivity.this, MessagesActivity.class);
                        intent.putExtra("companion", String.valueOf(dialogs.get(position).getCompanion()));
                        intent.putExtra("name", String.valueOf(dialogs.get(position).getName()));
                        startActivity(intent);
                    }
                });
                listView.setAdapter(messagesListAdapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
                        selectedItemPos = position;
                        new AlertDialog.Builder(MessagesListActivity.this)
                                .setMessage("Удалить диалог?")
                                .setCancelable(false)
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteDialog(position);
                                    }
                                })
                                .setNegativeButton("Нет", null)
                                .show();

                        return true;
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
