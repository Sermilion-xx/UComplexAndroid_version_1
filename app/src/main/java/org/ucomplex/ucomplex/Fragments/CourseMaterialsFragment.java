package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseMaterialsFragment extends ListFragment {

    ArrayList<ArrayList<File>> stackFiles = new ArrayList<>();
    private ArrayList<File> mItems;
    int level = 0;
    private Activity mContext;
    private boolean myFiles = false;
    private CourseMaterialsAdapter adapter;
    private int selectedItemPos;


    public void setAdapter(CourseMaterialsAdapter adapter) {
        this.adapter = adapter;
    }

    public ArrayList<File> getmItems() {
        return mItems;
    }

    public void setLevel(int level) {
        this.level = level;
        mItems = new ArrayList<>(stackFiles.get(level));
    }

    public ArrayList<ArrayList<File>> getStackFiles() {
        return stackFiles;
    }

    public void addStack(ArrayList<File> files){
        this.stackFiles.add(new ArrayList<>(files));
    }

    public CourseMaterialsAdapter getAdapter() {
        return adapter;
    }

    public boolean isMyFiles() {
        return myFiles;
    }

    public void levelUp(){
        this.level++;
    }

    public void levelDown(){
        this.level--;
    }

    public int getLevel() {
        return level;
    }

    public void setMyFiles(boolean myFiles) {
        this.myFiles = myFiles;
    }

    public CourseMaterialsFragment() {
        // Required empty public constructor
    }

    public ArrayList<File> getFiles() {
        return mItems;
    }

    public void addFile(File file) {
        this.mItems.add(file);
    }

    public void setFiles(ArrayList<File> files) {
        this.mItems = files;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        adapter = new CourseMaterialsAdapter(getActivity(), mItems);
        setListAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==getListView().getId()) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_my_files_files, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedItemPos = info.position;
        switch(item.getItemId()) {
            case R.id.edit:
                showInputDialog();
                return true;
            case R.id.delete:
                removeItem(selectedItemPos);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setDivider(null);
        registerForContextMenu(getListView());
//        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
//                if(myFiles) {
//                    selectedItemPos = position;
//                    getActivity().startActionMode(modeCallBack);
//                    view.setSelected(true);
//                }
//                return true;
//            }
//        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        File item = mItems.get(position);
        if (item.getType().equals("f")) {
            levelUp();
            Common.folderCode = item.getAddress();
            if(level>stackFiles.size()-1){
                if (!myFiles) {
                    FetchTeacherFilesTask fetchTeacherFilesTask = new FetchTeacherFilesTask(mContext, (OnTaskCompleteListener) mContext);
                    fetchTeacherFilesTask.setOwner(item.getOwner());
                    fetchTeacherFilesTask.setupTask(item.getAddress());
                } else {
                    FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(mContext, (OnTaskCompleteListener) mContext);
                    fetchMyFilesTask.setupTask(item.getAddress());
                }
            }else{
                mItems.clear();
                ArrayList<File> newFiles = new ArrayList<>(stackFiles.get(level));
                mItems.addAll(newFiles);
                this.adapter.notifyDataSetChanged();
            }
        } else {
            Common.folderCode = null;
            if (Common.isDownloadManagerAvailable()) {
                final String UC_BASE_URL = "https://chgu.org/files/users/" + String.valueOf(item.getOwner().getId()) + "/" + item.getAddress() + "." + item.getType();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(UC_BASE_URL));
                request.setDescription("Загрузка");
                request.setTitle(item.getName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, item.getName());
                DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getActivity(), "Идет загрузка...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Выберите дейсвтвие");
            mode.getMenuInflater().inflate(R.menu.menu_my_files_files, menu);
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int id = item.getItemId();
            switch (id) {
                case R.id.delete: {
                    removeItem(selectedItemPos);
                    mode.finish();
                    break;
                }
                case R.id.edit: {
                    showInputDialog();
                    mode.finish();
                    break;
                }
                default:
                    return false;
            }
            return false;
        }
    };

    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newName = editText.getText().toString();
                        renameItem(selectedItemPos, newName);
                    }
                }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void removeItem(final int pos){
        new AsyncTask<Void, Void, ArrayList>(){
            @Override
            protected ArrayList doInBackground(Void... params) {
                String url = "http://you.com.ru/student/my_files/delete_file?mobile=1";
                HashMap<String, String> httpParams = new HashMap();
                httpParams.put("file", mItems.get(pos).getAddress());
                Common.httpPost(url, Common.getLoginDataFromPref(mContext),httpParams);
                return null;
            }
            @Override
            protected void onPostExecute(ArrayList newFile) {
                super.onPostExecute(newFile);
                mItems.remove(pos);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void renameItem(final int pos, final String newName){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                String jsonData;
                String url = "http://you.com.ru/student/my_files/rename_file?mobile=1";
                HashMap<String, String> httpParams = new HashMap();
                httpParams.put("file", mItems.get(pos).getAddress());
                httpParams.put("name", newName);
                jsonData = Common.httpPost(url, Common.getLoginDataFromPref(mContext),httpParams);
                return jsonData;
            }
            @Override
            protected void onPostExecute(String newFile) {
                super.onPostExecute(newFile);
                if(newFile!=null){
                    mItems.get(pos).setName(newName);
                    adapter.notifyDataSetChanged();
                }else{
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Ошибка", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        }.execute();
    }
}


