package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Adaptors.CourseCalendarBeltAdapter;

import java.util.ArrayList;


public class CalendarBeltFragment extends ListFragment {

    private ArrayList<Quartet<Integer, String, String, Integer>> feedItems = new ArrayList<>();
    private CourseCalendarBeltAdapter courseCalendarBeltAdapter;

    public void setFeedItems(ArrayList<Quartet<Integer, String, String, Integer>> feedItems) {
        this.feedItems = feedItems;
    }

    public CourseCalendarBeltAdapter getCourseCalendarBeltAdapter() {
        return courseCalendarBeltAdapter;
    }

    public void setCourseCalendarBeltAdapter(CourseCalendarBeltAdapter courseCalendarBeltAdapter) {
        this.courseCalendarBeltAdapter = courseCalendarBeltAdapter;
    }

    public void initAdapter(Activity activity){
        courseCalendarBeltAdapter = new CourseCalendarBeltAdapter(activity, feedItems);
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
        if(courseCalendarBeltAdapter==null) {
            courseCalendarBeltAdapter = new CourseCalendarBeltAdapter(getActivity(), feedItems);
        }
        setListAdapter(courseCalendarBeltAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Quartet<Integer, String, String, Integer> item = feedItems.get(position);
        Toast.makeText(getActivity(), item.getValue1(), Toast.LENGTH_SHORT).show();
    }



}
