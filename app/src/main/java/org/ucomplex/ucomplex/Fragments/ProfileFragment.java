package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Adaptors.ProfileAdapter;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.List;

/**
 * Created by Sermilion on 20/02/16.
 */
public class ProfileFragment extends ListFragment {

    ProfileAdapter mAdapter;
    Activity mContext;
    Bitmap mBitmap;
    int mPerson = -1;
    User mUser;
    List<Triplet> mItems;

    public ProfileFragment() {

    }

    public ProfileAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmItems(List<Triplet> mItems) {
        this.mItems = mItems;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setPerson(int mPerson) {
        this.mPerson = mPerson;
    }

    public void setContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ProfileAdapter(mContext, mItems, mBitmap);
        setListAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }


}
