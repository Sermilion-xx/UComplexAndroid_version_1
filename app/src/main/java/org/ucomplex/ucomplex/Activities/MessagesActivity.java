package org.ucomplex.ucomplex.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMessagesTask;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Adaptors.MessagesAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.R;

import java.util.Collections;
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
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Сообщения");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        companion = getIntent().getStringExtra("companion");
        name = getIntent().getStringExtra("name");
        messagesAdapter = new MessagesAdapter(this, messageArrayList, companion, name);
        listView = (ListView) findViewById(R.id.list_messages_listview);
        listView.setScrollingCacheEnabled(false);
        fetchNewMessagesTask = new FetchMessagesTask(this, this);
        fetchNewMessagesTask.setType(0);
        fetchNewMessagesTask.setupTask(companion);

        Button sendMsgButton = (Button) findViewById(R.id.messages_send_button);
        final TextView messageTextView = (TextView) findViewById(R.id.messages_text);
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fetchNewMessagesTask = new FetchMessagesTask(MessagesActivity.this, MessagesActivity.this);
                fetchNewMessagesTask.setType(1);
                final String message = messageTextView.getText().toString();
                if (file) {
                    String[] splitFilename = filePath.split("/");
                    String filename = splitFilename[splitFilename.length - 1];
                    fetchNewMessagesTask.setupTask(filePath, companion, filename, message);
                } else {
                    fetchNewMessagesTask.setupTask(companion, messageTextView.getText().toString());
                }
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
                    }, 0, 3000);
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
                    super.onActivityResult(requestCode, resultCode, data);
                    if (null == data) return;
                    Uri originalUri = null;
                    if (requestCode == Common.GALLERY_INTENT_CALLED) {
                        originalUri = data.getData();
                    } else if (requestCode == Common.GALLERY_KITKAT_INTENT_CALLED) {
                        originalUri = data.getData();
                        this.grantUriPermission("org.ucomplex.ucomplex.Activities", originalUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    filePath = getPath(originalUri);
//                    byte[] fileByte = Common.fileToByte(filePath);
//                    if (fileByte != null) {
//                        contentBody = new ByteArrayBody(fileByte, "filename");
//                    }
                    this.revokeUriPermission(originalUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                        Collections.reverse(messageArrayList);
                        if (messageArrayList != null) {
                            messagesAdapter = new MessagesAdapter(this, messageArrayList, companion, name);
                            listView.setAdapter(messagesAdapter);
                            listView.setSelection(messagesAdapter.getCount() - 1);
                        }
                    } else if (fmt.getType() == 1 || fmt.getType() == 2) {
                        LinkedList result = (LinkedList) task.get();
                        if (result != null) {
                            int cycles = 0;
                            if (result.size() > 0) {
                                if (result.get(result.size() - 1) instanceof Bitmap) {
                                    cycles = result.size() - 1;
                                } else {
                                    cycles = result.size();
                                }
                            }

                            if (cycles > 0) {
                                messageArrayList.addLast((Message) result.get(0));
                                if (result.size() > 0) {
                                    messagesAdapter.notifyDataSetChanged();
                                    listView.setSelection(messagesAdapter.getCount() - 1);
                                } else {
                                    Toast.makeText(this, "Ошибка при отправке сообщения", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }
                    }
                    fetchNewMessagesTask = null;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
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
}
