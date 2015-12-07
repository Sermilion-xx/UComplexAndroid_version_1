package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ucomplex.ucomplex.R;


public class CalendarBeltFragment extends Fragment{

    private int gcourse;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_material, container, false);
    }

}
