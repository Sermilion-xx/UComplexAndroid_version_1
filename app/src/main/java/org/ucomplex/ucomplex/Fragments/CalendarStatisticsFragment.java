package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Adaptors.CalendarStatisticsAdapter;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 16/03/16.
 */
public class CalendarStatisticsFragment extends ListFragment {

    ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItems;

    public CalendarStatisticsFragment() {

    }

    public void setStatisticItems(ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItems) {
        this.statisticItems = statisticItems;

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.activity_background));
        getListView().setDivider(null);
        setListShown(true);
        if (statisticItems != null) {
            CalendarStatisticsAdapter calendarStatisticsAdapter = new CalendarStatisticsAdapter(getContext(), this.statisticItems);
            getListView().setAdapter(calendarStatisticsAdapter);
        }
        if((statisticItems != null ? statisticItems.size() : 0) ==0){
            getListView().setDivider(null);
        }else{
            getListView().setDivider(ContextCompat.getDrawable(getContext(), R.color.uc_gray_text_events));
            getListView().setDividerHeight(1);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
