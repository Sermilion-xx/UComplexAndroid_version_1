package org.ucomplex.ucomplex.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
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
    AsyncTask<Void, Void, ArrayList> uploadFileTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titles.add("Материалы");
        toolbar.setTitle(titles.get(0));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(this, this);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        fetchMyFilesTask.setupTask();
    }

    @Override
    public void onBackPressed() {
        if(uploadFileTask!=null){
            uploadFileTask.cancel(true);
        }
        linlaHeaderProgress.setVisibility(View.INVISIBLE);
        if (titles.size() > 1) {
            toolbar.setTitle(titles.get(titles.size() - 2));
            titles.remove(titles.size() - 1);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                if(uploadFileTask!=null){
                    uploadFileTask.cancel(true);
                }
                linlaHeaderProgress.setVisibility(View.INVISIBLE);
                return true;
            case R.id.my_files_add_file:
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Выберите документ"), Common.GALLERY_INTENT_CALLED);
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
                FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(MyFilesActivity.this, MyFilesActivity.this);
                fetchMyFilesTask.setupTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void createFolder(final String folderName) {
        if (folderName != null && !folderName.equals("")) {
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
                    if (newFolder != null) {
                        courseMaterialsFragment.addFile((File) newFolder.get(0));
                        courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                    } else {
                        Toast.makeText(MyFilesActivity.this, "Ошибка при создании папки.", Toast.LENGTH_SHORT)
                                .show();
                    }
                    linlaHeaderProgress.setVisibility(View.GONE);
                }
            }.execute();
        } else {
            Toast.makeText(this, "Введите название.", Toast.LENGTH_SHORT)
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
                        if (Common.isNetworkConnected(MyFilesActivity.this)) {
                            String folderName = editText.getText().toString();
                            createFolder(folderName);
                        } else {
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

    private void uplodFile(final String filePath) {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        uploadFileTask = new AsyncTask<Void, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(Void... params) {
                String jsonData = "";
                if (Common.folderCode != null) {
                    jsonData = Common.uploadFile(filePath, Common.getLoginDataFromPref(MyFilesActivity.this), Common.folderCode);

                } else {
                    jsonData = Common.uploadFile(filePath, Common.getLoginDataFromPref(MyFilesActivity.this));
                }
                return Common.getFileDataFromJson(jsonData, MyFilesActivity.this);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                uploadFileTask.cancel(true);
                linlaHeaderProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onPostExecute(ArrayList newFile) {
                super.onPostExecute(newFile);
                linlaHeaderProgress.setVisibility(View.GONE);
                if(newFile!=null){
                    if (courseMaterialsFragment != null) {
                        courseMaterialsFragment.addFile((File) newFile.get(0));
                        courseMaterialsFragment.getAdapter().notifyDataSetChanged();
                    }
                }
                uploadFileTask = null;
                cancel(true);
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
            String path = getPath(this, originalUri);
            if (Common.isNetworkConnected(this)) {
                uplodFile(path);
            } else {
                Toast.makeText(this, "Проверте интернет соединение", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Ошибка выбора файла", Toast.LENGTH_LONG).show();
        }
        this.revokeUriPermission(originalUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }

        }// MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
                courseMaterialsFragment.setMyFilesToolBarTitle(titles.get(titles.size() - 1));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_my_files, courseMaterialsFragment);
                if (!first)
                    fragmentTransaction.addToBackStack("Мои файлы");
                else
                    first = false;
                fragmentTransaction.commitAllowingStateLoss();

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
