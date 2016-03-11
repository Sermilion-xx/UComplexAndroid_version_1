package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

public class CourseMaterialsFragment extends ListFragment {


    private ArrayList<File> mItems;
    OnHeadlineSelectedListener mCallback;

    private Activity mContext;
    private boolean myFiles = false;
    private CourseMaterialsAdapter adapter;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    String myFilesToolBarTitle;


    public void setMyFilesToolBarTitle(String myFilesToolBarTitle) {
        this.myFilesToolBarTitle = myFilesToolBarTitle;
    }

    public String getMyFilesToolBarTitle() {
        return myFilesToolBarTitle;
    }

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
        //form Materials menu
        if (adapter == null) {
            adapter = new CourseMaterialsAdapter(getActivity(), mItems, myFiles, this);
        }
        setListAdapter(adapter);
    }


    public interface OnHeadlineSelectedListener {
        void onFolderSelect(String title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setListShown(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        File item = mItems.get(position);
        if (item.getType().equals("f")) {
            mCallback.onFolderSelect(item.getName());
            adapter.levelUp();
            Common.folderCode = item.getAddress();
            if (adapter.level > adapter.stackFiles.size() - 1) {
                if (!myFiles) {
                    new FetchTeacherFilesTask(mContext, (OnTaskCompleteListener) mContext) {
                        @Override
                        protected void onPostExecute(ArrayList fileArrayList) {
                            mItems.clear();
                            ArrayList<File> newFiles = new ArrayList<>(fileArrayList);
                            mItems.addAll(newFiles);
                            adapter.stackFiles.add(newFiles);
                            adapter.notifyDataSetChanged();
                        }
                    }.execute(item.getAddress(), item.getOwner());
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
            startDownload(item);
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

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    private void startDownload(File item) {
        String url = "http://storage.ucomplex.org/files/users/683/821dd76f2f47f08a.docx";
        new DownloadFileAsync().execute(item);
    }


    class DownloadFileAsync extends AsyncTask<File, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Downloading file..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(File... item) {

            final String UC_BASE_URL = "http://storage.ucomplex.org/files/users/" + String.valueOf(item[0].getOwner().getId()) + "/" + item[0].getAddress() + "." + item[0].getType();
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UC_BASE_URL)));
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
        }
    }


}


