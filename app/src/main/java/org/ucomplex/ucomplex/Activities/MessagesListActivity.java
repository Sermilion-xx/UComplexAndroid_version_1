package org.ucomplex.ucomplex.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchDialogsTask;
import org.ucomplex.ucomplex.Adaptors.MessagesListAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Dialog;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MessagesListActivity extends AppCompatActivity implements OnTaskCompleteListener {

    ArrayList<Dialog> dialogs = new ArrayList<>();
    MessagesListAdapter messagesListAdapter;
    private int selectedItemPos;
    LinearLayout linlaHeaderProgress;

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

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_message_list, menu);
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int id = item.getItemId();
            switch (id) {
                case R.id.action_message_delete: {
                    deleteDialog(selectedItemPos);
                    mode.finish();
                    break;
                }
                default:
                    return false;
            }
            return false;
        }
    };

    public void deleteDialog(final int position) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HashMap<String, String> httpParas = new HashMap<>();
                httpParas.put("id", String.valueOf(dialogs.get(position).getId()));
                String url = "https://ucomplex.org/user/messages/del_dialog?mobile=1";
                Common.httpPost(url, Common.getLoginDataFromPref(MessagesListActivity.this), httpParas);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
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
                        MessagesListActivity.this.startActionMode(modeCallBack);
                        view.setSelected(true);
                        return true;
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
