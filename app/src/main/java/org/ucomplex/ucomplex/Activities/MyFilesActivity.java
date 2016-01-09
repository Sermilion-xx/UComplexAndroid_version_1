package org.ucomplex.ucomplex.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyFilesActivity extends AppCompatActivity implements OnTaskCompleteListener {

    private ArrayList<File> mItems;
    boolean first = true;
    CourseMaterialsFragment courseMaterialsFragment;
    ProgressDialog dialog;
    String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(this,this);
        fetchMyFilesTask.setupTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.my_files_add_file:
                showFileChooser();
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
        new AsyncTask<Void, Void, ArrayList>(){

            @Override
            protected ArrayList doInBackground(Void... params) {
                String url = "http://you.com.ru/student/my_files/create_folder?mobile=1";
                HashMap<String, String> httpParams = new HashMap<String, String>();
                httpParams.put("name", folderName);
                if(Common.folderCode!=null){
                    httpParams.put("folder", Common.folderCode);
                }
                String jsonData = Common.httpPost(url, Common.getLoginDataFromPref(MyFilesActivity.this),httpParams);
                ArrayList<File> newFolder = Common.getFileDataFromJson(jsonData, MyFilesActivity.this);
                return newFolder;
            }
            @Override
            protected void onPostExecute(ArrayList newFolder) {
                super.onPostExecute(newFolder);
                courseMaterialsFragment.addFile((File) newFolder.get(0));
                courseMaterialsFragment.getAdapter().notifyDataSetChanged();
            }
        }.execute();
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
                        String folderName = editText.getText().toString();
                        createFolder(folderName);
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
        dialog = ProgressDialog.show(MyFilesActivity.this, "",
                "Файл загружается...", true);
        dialog.show();
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
                dialog.dismiss();
                courseMaterialsFragment.addFile((File) newFile.get(0));
                courseMaterialsFragment.getAdapter().notifyDataSetChanged();
            }
        }.execute();
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
            Toast.makeText(this, "Файловый менеджер не установлен.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Common.FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = null;
                    try {
                        path = Common.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    uplodFile(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                    mItems = (ArrayList) task.get();
                    courseMaterialsFragment = new CourseMaterialsFragment();
                    courseMaterialsFragment.setFiles(mItems);
                    courseMaterialsFragment.setMyFiles(true);
                    courseMaterialsFragment.setmContext(MyFilesActivity.this);

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
}
