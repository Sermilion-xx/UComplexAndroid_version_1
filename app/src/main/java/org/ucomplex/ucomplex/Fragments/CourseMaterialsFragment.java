package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchMyFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.File;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseMaterialsFragment extends ListFragment{

    private ArrayList<File> mItems;
    private Activity mContext;
    private boolean myFiles = false;
    private CourseMaterialsAdapter adapter;

    public void setAdapter(CourseMaterialsAdapter adapter) {
        this.adapter = adapter;
    }

    public CourseMaterialsAdapter getAdapter() {
        return adapter;
    }

    public boolean isMyFiles() {
        return myFiles;
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

    public void addFile(File file){
        this.mItems.add(file);
    }

    public void setFiles(ArrayList<File> files) {
        this.mItems = files;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mItems==null){
            mItems = new ArrayList<>();
        }
        adapter = new CourseMaterialsAdapter(getActivity(), mItems);
        setListAdapter(adapter);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                new AsyncTask<Void, Void, ArrayList>(){

                    @Override
                    protected ArrayList doInBackground(Void... params) {
                        String jsonData = "";
                        String url = "http://you.com.ru/student/my_files/delete_file?mobile=1";
                        HashMap<String, String> httpParams = new HashMap();
                        httpParams.put("file", mItems.get(pos).getAddress());
                        jsonData = Common.httpPost(url, Common.getLoginDataFromPref(mContext),httpParams);
                        return null;
                    }
                    @Override
                    protected void onPostExecute(ArrayList newFile) {
                        super.onPostExecute(newFile);
                        mItems.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                }.execute();


                return true;
            }
        });
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        File item = mItems.get(position);
        if(item.getType().equals("f")){
            Common.folderCode = item.getAddress();
            if(!myFiles){
                FetchTeacherFilesTask fetchTeacherFilesTask = new FetchTeacherFilesTask(mContext, (OnTaskCompleteListener) mContext);
                fetchTeacherFilesTask.setOwner(item.getOwner());
                fetchTeacherFilesTask.setupTask(item.getAddress());

            }else{
                FetchMyFilesTask fetchMyFilesTask = new FetchMyFilesTask(mContext, (OnTaskCompleteListener) mContext);
                fetchMyFilesTask.setupTask(item.getAddress());
            }

        }else{
            Common.folderCode = null;
            if(Common.isDownloadManagerAvailable(getContext())) {
                final String UC_BASE_URL = "https://chgu.org/files/users/" +String.valueOf(item.getOwner().getId())+"/"+ item.getAddress() +"."+ item.getType();
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
}
