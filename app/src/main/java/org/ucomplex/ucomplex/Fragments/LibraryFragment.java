package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Activities.LibraryActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchLibraryTask;
import org.ucomplex.ucomplex.Adaptors.LibraryAdapter;

import java.util.ArrayList;


public class LibraryFragment extends ListFragment {

    ArrayList libraryData;
    int type;
    LibraryActivity libraryActivity;

    public LibraryActivity getLibraryActivity() {
        return libraryActivity;
    }

    public void setLibraryActivity(LibraryActivity libraryActivity) {
        this.libraryActivity = libraryActivity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList getLibraryData() {
        return libraryData;
    }

    public void setLibraryData(ArrayList libraryData) {
        this.libraryData = libraryData;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new LibraryAdapter(getActivity(), libraryData,type));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Quintet<Integer, String, String, Integer,Integer> item = (Quintet<Integer, String, String, Integer, Integer>) libraryData.get(position);
        int type = 0;
        if(position==0){
            type=1;
        }
        FetchLibraryTask fetchLibraryTask = new FetchLibraryTask(this.libraryActivity,this.libraryActivity);
        fetchLibraryTask.setupTask(type);
    }

}
