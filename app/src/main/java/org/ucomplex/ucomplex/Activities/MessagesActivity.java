package org.ucomplex.ucomplex.Activities;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMessagesTask;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.Adaptors.MessagesAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends AppCompatActivity implements OnTaskCompleteListener {

    LinkedList messageArrayList = new LinkedList<>();
    ListView listView;
    String companion;
    MessagesAdapter messagesAdapter;
    String name;
    String filePath;
    boolean file = false;
    FetchMessagesTask fetchNewMessagesTask;
    TextView nameTextView;
    CircleImageView profileImageView;
    TextView messageTextView;
    Button sendFileButton;
    Button sendMsgButton;
    CircleImageView messageImage;
    CircleImageView messageImageTemp;
    ProgressBar imageProgress;
    boolean first;
    boolean fetching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface robotoFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
        profileImageView = (CircleImageView) findViewById(R.id.list_messages_toolbar_profile);
        nameTextView = (TextView) findViewById(R.id.list_messages_toolbar_name);
        messageImage = (CircleImageView) findViewById(R.id.message_image);
        messageImageTemp = (CircleImageView) findViewById(R.id.message_image_temp);
        imageProgress = (ProgressBar) findViewById(R.id.imagep_rogress);
        imageProgress.setVisibility(View.INVISIBLE);
        nameTextView.setTypeface(robotoFont);
        companion = getIntent().getStringExtra("companion");
        name = getIntent().getStringExtra("name");
        Bitmap bmp = getIntent().getParcelableExtra("profileImage");
        User aUser = new User();
        aUser.setId(Integer.valueOf(companion));
        aUser.setName(name);
        if(bmp==null){
            profileImageView.setImageDrawable(Common.getDrawable(aUser));
        }else{
            profileImageView.setImageBitmap((Bitmap)getIntent().getParcelableExtra("profileImage"));
        }

        String[] aName = name.split(" ");
        if(aName.length>1){
            nameTextView.setText(aName[1] + " " + aName[0]);
        }else{
            nameTextView.setText(aName[0]);
        }
        messagesAdapter = new MessagesAdapter(this, messageArrayList, companion, name);
        listView = (ListView) findViewById(R.id.list_messages_listview);
        listView.setScrollingCacheEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Message message = (Message) messagesAdapter.getItem(position);
                if(message.getFiles().size()>0){
                    String[] typeArray = ((Message) messagesAdapter.getItem(position)).getFiles().get(0).getName().split("\\.");
                    String type = typeArray[typeArray.length-1];
                    if(!type.equals("jpg") && !type.equals("png")){
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        new AsyncTask<Void, Void, Bitmap>(){
                                            @Override
                                            protected Bitmap doInBackground(Void... params) {
                                                final String UC_BASE_URL = "http://storage.ucomplex.org/files/messages/" + ((Message) messagesAdapter.getItem(position)).getFiles().get(0).getFrom() + "/" + ((Message) messagesAdapter.getItem(position)).getFiles().get(0).getAddress();
                                                MessagesActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UC_BASE_URL)));
                                                return null;
                                            }
                                        }.execute();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MessagesActivity.this);
                        builder.setMessage("Сохранить документ?").setPositiveButton("Да", dialogClickListener)
                                .setNegativeButton("Нет", dialogClickListener).show();

                    }else if (((Message) messagesAdapter.getItem(position)).getMessageImage()!=null){
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:

                                        View v = messagesAdapter.getViewByPosition(position, listView);
                                        final MessagesAdapter.ViewHolder vh = (MessagesAdapter.ViewHolder) v.getTag();
                                        vh.getFileProgressBar().setVisibility(View.VISIBLE);
                                        new AsyncTask<Void, Void, Bitmap>(){

                                            @Override
                                            protected Bitmap doInBackground(Void... params) {
                                                String url = "http://storage.ucomplex.org/files/messages/" + ((Message) messagesAdapter.getItem(position)).getFiles().get(0).getFrom() + "/" + ((Message) messagesAdapter.getItem(position)).getFiles().get(0).getAddress();
                                                Bitmap bitmap = Common.getBitmapFromURL(url,1);
                                                return bitmap;
                                            }

                                            @Override
                                            protected void onPostExecute(Bitmap aVoid) {
                                                super.onPostExecute(aVoid);
                                                vh.getFileProgressBar().setVisibility(View.INVISIBLE);
                                                MediaStore.Images.Media.insertImage(getContentResolver(), aVoid, ((Message) messagesAdapter.getItem(position)).getFiles().get(0).getName() , "");
                                                Toast.makeText(MessagesActivity.this, "Фото сохранено.", Toast.LENGTH_LONG)
                                                        .show();

                                            }
                                        }.execute();


                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MessagesActivity.this);
                        builder.setMessage("Сохранить фото?").setPositiveButton("Да", dialogClickListener)
                                .setNegativeButton("Нет", dialogClickListener).show();

                    }

                }
            }
        });
        fetchNewMessagesTask = new FetchMessagesTask(this, this);
        fetchNewMessagesTask.setType(0);
        fetchNewMessagesTask.setupTask(companion);
        first = true;

        sendMsgButton = (Button) findViewById(R.id.messages_send_button);
        sendMsgButton.setEnabled(false);
        messageTextView = (TextView) findViewById(R.id.messages_text);
        messageTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendMsgButton.setEnabled(true);

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fetchNewMessagesTask = new FetchMessagesTask(MessagesActivity.this, MessagesActivity.this);
                fetchNewMessagesTask.setType(1);
                String message = messageTextView.getText().toString();
                if (file && filePath != null) {
                    String[] splitFilename = filePath.split("/");
                    String filename = splitFilename[splitFilename.length - 1];
                    messageImageTemp.setImageBitmap(null);
                    fetchNewMessagesTask.setupTask(filePath, companion, filename, message);
                    imageProgress.setVisibility(View.VISIBLE);
                    file = false;
                } else {
                    fetchNewMessagesTask.setupTask(companion, messageTextView.getText().toString());
                    imageProgress.setVisibility(View.VISIBLE);
                }
                ObjectAnimator.ofFloat(sendFileButton, "rotation", 1, 0).start();
                messageTextView.setText("");
                scrollMyListViewToBottom();
            }
        });
        sendFileButton = (Button) findViewById(R.id.messages_file_button);
        sendFileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!file){
                    showFileChooser();
                }else{
                    file = false;
                    filePath = "";
                    messageTextView.setText("");
                    messageImageTemp.setImageBitmap(null);
                }
                ObjectAnimator.ofFloat(sendFileButton, "rotation", 1, 0).start();
            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!fetching) {
                    if (fetchNewMessagesTask == null) {
                        fetchNewMessagesTask = new FetchMessagesTask(MessagesActivity.this, MessagesActivity.this);
                        fetchNewMessagesTask.setType(0);
                        fetchNewMessagesTask.setupTask(companion);
                        fetching = true;
                    }
                }
            }
        }, 0, 10000);
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
                    String[] splitFilename = filePath.split("/");
                    String filename = splitFilename[splitFilename.length - 1];
                    String[] fileNameArray = filename.split("\\.");
                    String format = fileNameArray[fileNameArray.length-1];
                    file = true;
                    sendMsgButton.setEnabled(true);

                    ObjectAnimator.ofFloat(sendFileButton, "rotation", 1, 45).start();

                    if(format.equals("jpeg") || format.equals("jpg") || format.equals("png")){
                        java.io.File image = new java.io.File(filePath);
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                        if(bitmap!=null){
                            messageImageTemp.setImageBitmap(bitmap);
                        }
                    }else{
                        messageImageTemp.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_attachment_dark));
                    }
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
                    if(fmt.getType() == 0){
                        messageArrayList = (LinkedList) task.get();
                        if(messageArrayList != null){
                            if(!first){
                                for(int i=0; i<messageArrayList.size(); i++){
                                    if(messageArrayList.get(i) instanceof Bitmap){
                                        messageArrayList.remove(i);
                                    }
                                }
                                Collections.reverse(messageArrayList);
                                messagesAdapter.setValues(messageArrayList);
                                messagesAdapter.notifyDataSetChanged();
                                listView.setSelection(messagesAdapter.getCount()-1);
                            }else{
                                Collections.reverse(messageArrayList);
                                messagesAdapter = new MessagesAdapter(this, messageArrayList, companion, name);
                                listView.setAdapter(messagesAdapter);
                                listView.setSelection(messagesAdapter.getCount()-1);
                                if(messagesAdapter.getBitmap() !=null){
                                    profileImageView.setImageBitmap(messagesAdapter.getBitmap());
                                }
                                first = false;
                            }
                        }
                    }
                     else if (fmt.getType() == 1 || fmt.getType() == 2) {

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
                                messageArrayList.addLast(result.get(0));
                                if (result.size() > 0) {
                                    messagesAdapter.notifyDataSetChanged();
                                    listView.setSelection(messagesAdapter.getCount() - 1);

                                } else {
                                    Toast.makeText(this, "Ошибка при отправке сообщения", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }
                        imageProgress.setVisibility(View.INVISIBLE);
                    }
                    fetchNewMessagesTask = null;
                    fetching = false;
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
