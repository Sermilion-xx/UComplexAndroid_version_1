package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarBeltTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchUserEventsTask;
import org.ucomplex.ucomplex.Adaptors.CourseCalendarBeltAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.EventRowItem;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;


public class CalendarBeltFragment extends ListFragment{

    //mark
    private ArrayList<Quartet<Integer, String, String, Integer>> feedItems = new ArrayList<>();
    private CourseCalendarBeltAdapter courseCalendarBeltAdapter;
    private Activity mContext;
    private int gcourse;
    Button btnLoadExtra;

    public void setGcourse(int gcourse) {
        this.gcourse = gcourse;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public void setFeedItems(ArrayList<Quartet<Integer, String, String, Integer>> feedItems) {
        this.feedItems = feedItems;
    }

    public CourseCalendarBeltAdapter getCourseCalendarBeltAdapter() {
        return courseCalendarBeltAdapter;
    }

    public void initAdapter(Activity activity){
        if(feedItems!=null){
            if(feedItems.size()>0){
                courseCalendarBeltAdapter = new CourseCalendarBeltAdapter(activity, feedItems);
            }
        }else{
            setListShown(true);
        }
    }

    public CalendarBeltFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setBackgroundColor(Color.WHITE);
        setListShown(true);

        if(courseCalendarBeltAdapter==null) {
            if(feedItems!=null){
                courseCalendarBeltAdapter = new CourseCalendarBeltAdapter(getActivity(), feedItems);
            }
        }
        if(feedItems!=null){
            setListAdapter(courseCalendarBeltAdapter);
        }
        getListView().addFooterView(btnLoadExtra);
        if (courseCalendarBeltAdapter.getFeedItems().size() < 21) {
            btnLoadExtra.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnLoadExtra = new Button(getActivity());
        btnLoadExtra.setFocusable(false);
        btnLoadExtra.setText("Загрузить еще...");
        btnLoadExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(Common.isNetworkConnected(getContext())){
                    new FetchCalendarBeltTask(mContext) {
                        @Override
                        protected void onPostExecute(ArrayList fileArrayList) {
                            super.onPostExecute(fileArrayList);
                            if(fileArrayList!=null){
                                courseCalendarBeltAdapter.addAllFeedItems(fileArrayList);
                                courseCalendarBeltAdapter.notifyDataSetChanged();
                            }
                            if (courseCalendarBeltAdapter.getFeedItems() != null) {
                                if (courseCalendarBeltAdapter.getFeedItems().size() < 21) {
                                    btnLoadExtra.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.setupTask(gcourse, courseCalendarBeltAdapter.getFeedItems().size());
                }else {
                    Toast.makeText(getContext(), "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void checkLoadButton(ArrayList<Quartet<Integer, String, String, Integer>> items){
        if (courseCalendarBeltAdapter.getFeedItems() != null) {
            if (items.size() < 20) {
                btnLoadExtra.setVisibility(View.GONE);
            }else{
                btnLoadExtra.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(getActivity(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
}
