package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyFilesActivity extends AppCompatActivity implements OnTaskCompleteListener, CourseMaterialsFragment.OnHeadlineSelectedListener {

    private ArrayList<File> mItems;
    boolean first = true;
    CourseMaterialsFragment courseMaterialsFragment;
    LinearLayout linlaHeaderProgress;
    Toolbar toolbar;
    ArrayList<String> titles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titles.add("Материалы");
        toolbar.setTitle(titles.get(0));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(this,this);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        fetchMyFilesTask.setupTask();
    }

    @Override
    public void onBackPressed(){
        if(titles.size()>1){
            toolbar.setTitle(titles.get(titles.size()-2));
            titles.remove(titles.size()-1);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                if(courseMaterialsFragment.getAdapter().getLevel()==0){
//                    toolbar.setTitle("Материалы");
//                }else{
//                    toolbar.setTitle(courseMaterialsFragment.getFiles().get(courseMaterialsFragment.getAdapter().getLevel()+1).getName());
//                }
                return true;
            case R.id.my_files_add_file:
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Выберите документ"),Common.GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    startActivityForResult(intent, Common.GALLERY_KITKAT_INTENT_CALLED);
                }
                return true;
            case R.id.my_files_add_folder:
                showInputDialog();
                return true;
            case R.id.my_files_refresh:
                FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(MyFilesActivity.this,MyFilesActivity.this);
                fetchMyFilesTask.setupTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void createFolder(final String folderName){
        if (folderName != null && !folderName.equals("")){
            new AsyncTask<Void, Void, ArrayList>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                }

                @Override
                protected ArrayList doInBackground(Void... params) {
                    String url = "http://you.com.ru/student/my_files/create_folder?mobile=1";
                    HashMap<String, String> httpParams = new HashMap<>();
                    httpParams.put("name", folderName);
                    if (Common.folderCode != null) {
                        httpParams.put("folder", Common.folderCode);
                    }
                    String jsonData = Common.httpPost(url, Common.getLoginDataFromPref(MyFilesActivity.this), httpParams);
                    ArrayList<File> newFolder = Common.getFileDataFromJson(jsonData, MyFilesActivity.this);
                    return newFolder;
                }

                @Override
                protected void onPostExecute(ArrayList newFolder) {
                    super.onPostExecute(newFolder);
                    if(newFolder!=null){
                        courseMaterialsFragment.addFile((File) newFolder.get(0));
                        courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                    }else{
                        Toast.makeText(MyFilesActivity.this, "Ошибка при создании папки.", Toast.LENGTH_SHORT)
                                .show();
                    }
                    linlaHeaderProgress.setVisibility(View.GONE);
                }
            }.execute();
        }else{
            Toast.makeText(this, "Введите название для новой папки.", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_materials, menu);
        return true;
    }

    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MyFilesActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyFilesActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(Common.isNetworkConnected(MyFilesActivity.this)){
                            String folderName = editText.getText().toString();
                            createFolder(folderName);
                        }else {
                            Toast.makeText(MyFilesActivity.this, "Проверьте интернет соединение.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void uplodFile(final String filePath){
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, ArrayList>(){

            @Override
            protected ArrayList doInBackground(Void... params) {
                String jsonData = "";
                if(Common.folderCode!=null){
                    jsonData = Common.uploadFile(filePath, Common.getLoginDataFromPref(MyFilesActivity.this), Common.folderCode);

                }else{
                    jsonData = Common.uploadFile(filePath, Common.getLoginDataFromPref(MyFilesActivity.this));
                }
                return Common.getFileDataFromJson(jsonData, MyFilesActivity.this);
            }
            @Override
            protected void onPostExecute(ArrayList newFile) {
                super.onPostExecute(newFile);
                linlaHeaderProgress.setVisibility(View.GONE);
                if(courseMaterialsFragment!=null) {
                    courseMaterialsFragment.addFile((File) newFile.get(0));
                    courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                }
            }
        }.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (null == data) return;
        Uri originalUri = null;
        if (requestCode == Common.GALLERY_INTENT_CALLED) {
            originalUri = data.getData();
        } else if (requestCode == Common.GALLERY_KITKAT_INTENT_CALLED) {
            originalUri = data.getData();
            this.grantUriPermission("org.ucomplex.ucomplex.Activities", originalUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if (originalUri != null) {
            if(Common.isNetworkConnected(this)){
                uplodFile(getPath(originalUri));
            }else {
                Toast.makeText(this, "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
            }
        }
        this.revokeUriPermission(originalUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                linlaHeaderProgress.setVisibility(View.GONE);
                    mItems = (ArrayList) task.get();
                    courseMaterialsFragment = new CourseMaterialsFragment();
                    courseMaterialsFragment.setFiles(mItems);
                    courseMaterialsFragment.setMyFiles(true);
                    courseMaterialsFragment.setmContext(MyFilesActivity.this);
                    courseMaterialsFragment.setMyFilesToolBarTitle(titles.get(titles.size()-1));

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

    @Override
    public void onFolderSelect(String title) {
        toolbar.setTitle(title);
        titles.add(title);
    }
}
