package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ucomplex.ucomplex.R;

/**
 * Created by Sermilion on 29/02/16.
 */
public class SettingsTwoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_two, container, false);
        return rootView;
    }
}
