package org.ucomplex.ucomplex.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.CourseActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchTeacherFilesTask;
import org.ucomplex.ucomplex.Adaptors.CourseMaterialsAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;

public class CourseMaterialsFragment extends ListFragment{

    ArrayList<File> mItems;
    CourseActivity mContext;

    public CourseMaterialsFragment() {
        // Required empty public constructor
    }

    public ArrayList<File> getFiles() {
        return mItems;
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
        setListAdapter(new CourseMaterialsAdapter(getActivity(), mItems));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        File item = mItems.get(position);
        if(item.getType().equals("f")){
            FetchTeacherFilesTask fetchTeacherFilesTask = new FetchTeacherFilesTask(mContext, mContext);
            fetchTeacherFilesTask.setOwner(item.getOwner());
            fetchTeacherFilesTask.setupTask(item.getAddress());
        }else{
//        WebView mWebView=new WebView(getActivity());
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl("https://ucomplex.org/files/users/" +item.getOwner().getId()+"/"+ item.getAddress() +"."+ item.getType());
//        getActivity().setContentView(mWebView);

            if(isDownloadManagerAvailable(getContext())) {
                final String UC_BASE_URL = "https://chgu.org/files/users/" +String.valueOf(item.getOwner().getId())+"/"+ item.getAddress() +"."+ item.getType();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(UC_BASE_URL));
                request.setDescription("Загрузка");
                request.setTitle(item.getName());
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, item.getName());

// get download service and enqueue file
                DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getActivity(), "Идет загрузка...", Toast.LENGTH_SHORT).show();
            }

//            Common.getPdfFromURL(item.getAddress(), String.valueOf(item.getOwner().getId()), item.getType(), getActivity());
            // do something

        }

    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }




    public void setmContext(CourseActivity mContext) {
        this.mContext = mContext;
    }
}
