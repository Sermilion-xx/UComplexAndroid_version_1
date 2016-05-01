package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.UsersGroup;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 01/05/16.
 */
public class UsersGroupAdapter extends ArrayAdapter<UsersGroup> {


    ArrayList<UsersGroup> mUsersGroups;

    public UsersGroupAdapter(Context context, ArrayList<UsersGroup> usersGroups) {
        super(context, -1, usersGroups);
        mUsersGroups = usersGroups;
    }

    @Override
    public int getCount() {
        return mUsersGroups.size() > 0 ? mUsersGroups.size() : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return mUsersGroups.size() != 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public UsersGroup getItem(int position) {
        return mUsersGroups.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        if (mUsersGroups.size() == 0) {
            if (!Common.isNetworkConnected(getContext())) {
                convertView = mInflater.inflate(R.layout.list_item_no_internet, null, false);
            } else {
                convertView = mInflater.inflate(R.layout.list_item_no_content, null, false);
            }
            return convertView;
        } else {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_user_group, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mNameTextView = (TextView) convertView.findViewById(R.id.list_users_group_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            UsersGroup item = getItem(position);
            viewHolder.mNameTextView.setText(item.getName());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView mNameTextView;
    }
}

