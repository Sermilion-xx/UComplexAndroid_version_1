package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.CourseMaterialsFragment;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyFilesActivity extends AppCompatActivity implements OnTaskCompleteListener {

    private ArrayList<File> mItems;
    boolean first = true;
    CourseMaterialsFragment courseMaterialsFragment;
    ProgressDialog dialog;

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void uplodFile(final String filePath, final String ... folderCode){
        dialog = ProgressDialog.show(MyFilesActivity.this, "",
                "Файл загружается...", true);
        dialog.show();
        new AsyncTask<Void, Void, ArrayList>(){

            @Override
            protected ArrayList doInBackground(Void... params) {
                String jsonData = "";
                if(folderCode!=null){
                    jsonData = Common.uploadFile(filePath, Common.getLoginDataFromPref(MyFilesActivity.this), folderCode);
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
