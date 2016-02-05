package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseMaterialsFragment extends ListFragment {


    private ArrayList<File> mItems;

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


    public CourseMaterialsAdapter getAdapter() {
        return adapter;
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
        adapter = new CourseMaterialsAdapter(getActivity(), mItems, myFiles, this);
        setListAdapter(adapter);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        File item = mItems.get(position);
        if (item.getType().equals("f")) {
            adapter.levelUp();
            Common.folderCode = item.getAddress();
            if (adapter.level > adapter.stackFiles.size() - 1) {
                if (!myFiles) {
                    FetchTeacherFilesTask fetchTeacherFilesTask = new FetchTeacherFilesTask(mContext, (OnTaskCompleteListener) mContext);
                    fetchTeacherFilesTask.setOwner(item.getOwner());
                    fetchTeacherFilesTask.setupTask(item.getAddress());
                } else {
                    FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(mContext, (OnTaskCompleteListener) mContext);
                    fetchMyFilesTask.setupTask(item.getAddress());
                }
            } else {
                mItems.clear();
                ArrayList<File> newFiles = new ArrayList<>(adapter.stackFiles.get(adapter.level));
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == getListView().getId()) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_my_files_files, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(mContext, R.color.activity_background)));
        getListView().setDividerHeight(3);
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }
}


