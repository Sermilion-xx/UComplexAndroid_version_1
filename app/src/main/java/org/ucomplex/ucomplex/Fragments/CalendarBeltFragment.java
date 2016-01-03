package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Adaptors.CourseCalendarBeltAdapter;

import java.util.ArrayList;


public class CalendarBeltFragment extends ListFragment {

    private int gcourse;
    private ArrayList<Quartet<Integer, String, String, Integer>> feedItems = new ArrayList<>();

    public ArrayList<Quartet<Integer, String, String, Integer>> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(ArrayList<Quartet<Integer, String, String, Integer>> feedItems) {
        this.feedItems = feedItems;
    }

    public int getGcourse() {
        return gcourse;
    }

    public void setGcourse(int gcourse) {
        this.gcourse = gcourse;
    }

    public CalendarBeltFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new CourseCalendarBeltAdapter(getActivity(), feedItems));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        Quartet<Integer, String, String, Integer> item = feedItems.get(position);

        // do something
        Toast.makeText(getActivity(), item.getValue1(), Toast.LENGTH_SHORT).show();
    }



}
