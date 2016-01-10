package org.ucomplex.ucomplex.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMessagesTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Adaptors.MessagesAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.R;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MessagesActivity extends AppCompatActivity implements OnTaskCompleteListener {

    LinkedList<Message> messageArrayList = new LinkedList<>();
    ListView listView;
    String companion;
    MessagesAdapter messagesAdapter;
    String name;
    String filePath;
    boolean file = false;
    ByteArrayBody contentBody;
    FetchMessagesTask fetchNewMessagesTask;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    companion = getIntent().getStringExtra("companion");
                    name = getIntent().getStringExtra("name");
                    messagesAdapter = new MessagesAdapter(this, messageArrayList, companion, name);
                    setContentView(R.layout.activity_message);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    toolbar.setTitle("Сообщения");
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                            final String message = messageTextView.getText().toString();
                            if(file){
                                new AsyncTask<String,Void,Void>(){

                                    @Override
                                    protected Void doInBackground(String... params) {
                                        Common.sendFile(filePath, companion, message, params[0]);
                                        return null;
                                    }
                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                    }
                                }.execute(Common.getLoginDataFromPref(MessagesActivity.this));
                            }else{
                                sendMessageTask.setupTask(companion, messageTextView.getText().toString());
                            }

//                            Message messageObj  = new Message();
//                            messageObj.setFrom(Common.getUserDataFromPref(MessagesActivity.this).getPerson());
//                            messageObj.setMessage(message);
//                            messageObj.setName(name);
//                            messageObj.setStatus(0);
//                            DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
//                            Date date = new Date();
//                            messageObj.setTime(dateFormat.format(date));
//                            messageArrayList.add(messageObj);
//                            messagesAdapter.notifyDataSetChanged();
//                            messagesAdapter = new MessagesAdapter(MessagesActivity.this, messageArrayList);
//                            listView.setAdapter(messagesAdapter);
                            scrollMyListViewToBottom();
                        }
                    });


                    Button sendFileButton = (Button) findViewById(R.id.messages_file_button);
                    sendFileButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            file = true;
                            showFileChooser();
                        }
                    });

                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if(fetchNewMessagesTask==null) {
                                fetchNewMessagesTask = new FetchMessagesTask(MessagesActivity.this, MessagesActivity.this);
                                fetchNewMessagesTask.setType(2);
                                fetchNewMessagesTask.setupTask(companion);
                            }
                        }
                    }, 0, 4000);
    }

    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(messagesAdapter.getCount() - 1);
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Выберите файл для загрузки"),
                    Common.FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Файловый менеджер не установлен",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Common.FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = Common.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("", "File Path: " + path);
                    filePath = path;
                    byte[] fileByte = Common.fileToByte(path);
                    contentBody = new ByteArrayBody(fileByte, "filename");

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {

            Toast.makeText(this, "Операция была прервана", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                if (task instanceof UploadPhotoTask) {

                } else {
                    FetchMessagesTask fmt = (FetchMessagesTask) task;
                    if (fmt.getType() == 0) {
                        messageArrayList = (LinkedList) task.get();
//                        Collections.reverse(messageArrayList);
                        if(messageArrayList!=null){
                            messagesAdapter = new MessagesAdapter(this, messageArrayList, companion, name);
                            listView.setAdapter(messagesAdapter);
                            fetchNewMessagesTask = null;
                        }
                    } else if (fmt.getType() == 1 || fmt.getType() == 2) {
                        LinkedList result = (LinkedList) task.get();
                        int cycles = 0;
                        if(result.size()>0) {
                            if (result.get(result.size() - 1) instanceof Bitmap) {
                                cycles = result.size() - 1;
                            } else {
                                cycles = result.size();
                            }
                        }
                        if (result != null && cycles > 0) {
                            messageArrayList.addLast((Message) result.get(0));
                            if (result.size() > 0) {
                                messagesAdapter.notifyDataSetChanged();
//                                listView.setAdapter(messagesAdapter);
                                fetchNewMessagesTask = null;
                            } else {
                                Toast.makeText(this, "Ошибка при отправке сообщения", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }
                }
                }catch(InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }

        }
    }
}
