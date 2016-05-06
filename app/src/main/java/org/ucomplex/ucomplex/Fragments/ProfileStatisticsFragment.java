package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Adaptors.ProfileStatisticsAdapter;

import java.util.ArrayList;

/**
 * Created by Sermilion on 16/03/16.
 */
public class ProfileStatisticsFragment extends ListFragment {

    ArrayList<Pair<String, String>> statisticItems = new ArrayList<>();
    boolean isClosed;

    public ProfileStatisticsFragment() {
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public void setStatisticItems(ArrayList<Pair<String, String>> statisticItems) {
        this.statisticItems = statisticItems;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        setListShown(true);
        if (statisticItems != null) {
            ProfileStatisticsAdapter profileStatisticsAdapter = new ProfileStatisticsAdapter(getContext(), this.statisticItems);
            profileStatisticsAdapter.setClosed(isClosed);
            getListView().setAdapter(profileStatisticsAdapter);
        }
        if ((statisticItems != null ? statisticItems.size() : 0) == 0) {
            getListView().setDivider(null);
        } else {
            getListView().setDividerHeight(1);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
