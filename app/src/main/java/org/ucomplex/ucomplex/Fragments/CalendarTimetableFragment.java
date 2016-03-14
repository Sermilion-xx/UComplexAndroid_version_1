package org.ucomplex.ucomplex.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Adaptors.CalendarDayAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 14/03/16.
 */
public class CalendarTimetableFragment extends ListFragment {

    ArrayList calendarDay;

    public void setCalendarDay(ArrayList calendarDay) {
        this.calendarDay = calendarDay;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.activity_background));
        getListView().setDivider(null);
        setListShown(true);
        CalendarDayAdapter calendarDayAdapter = new CalendarDayAdapter(getContext(),calendarDay);
        getListView().setAdapter(calendarDayAdapter);
        getListView().setDividerHeight(0);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
