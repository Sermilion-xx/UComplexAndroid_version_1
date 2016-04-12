package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Activities.EventsActivity;
import org.ucomplex.ucomplex.Activities.ProfileStatisticsActivity;
import org.ucomplex.ucomplex.Adaptors.ProfileAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;

import java.lang.reflect.Field;
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
    int hasPhoto = 0;
    String code;
    User user;

    public ProfileFragment() {

    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setHasPhoto(int hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    public void setCode(String code) {
        this.code = code;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Common.getUserDataFromPref(mContext);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (hasPhoto == 1 && mBitmap == null) {
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    return Common.getBitmapFromURL(params[0], 0);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    mBitmap = bitmap;
                    mAdapter = new ProfileAdapter(mContext, mItems, mBitmap, user);
                    setListAdapter(mAdapter);
                }
            }.execute(code);
        } else {
            mAdapter = new ProfileAdapter(mContext, mItems, mBitmap, user);
            setListAdapter(mAdapter);
        }
        getListView().setDivider(null);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ProfileStatisticsActivity.class);
                intent.putExtra("role", (String) mItems.get(position).getValue2());
                intent.putExtra("name", mUser.getName());
                String role = (String) mItems.get(position).getValue1();
                if(!Common.isInt(role)){
                    String type = (String) mItems.get(position).getValue1();
                    intent.putExtra("type", type.split("/")[1]);
                }else {
                    intent.putExtra("type", (String) mItems.get(position).getValue1());
                }
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
