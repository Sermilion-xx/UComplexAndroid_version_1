package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sermilion on 10/12/2015.
 */
public class SubjectsAdapter extends ArrayAdapter<Triplet<String, String, Integer>> {

    private Typeface robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    LayoutInflater inflater;
    List<Triplet<String, String, Integer>> mItems;

    public SubjectsAdapter(Context context, List<Triplet<String, String, Integer>> items) {
        super(context, R.layout.list_item_subject, items);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size()>0?mItems.size():1;
    }
    @Override
    public boolean isEnabled(int position) {
        return mItems.size() != 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        inflater = LayoutInflater.from(getContext());
        if (mItems.size()==0){
            if(!Common.isNetworkConnected(getContext())){
                convertView = inflater.inflate(R.layout.list_item_no_internet, null, false);
            }else{
                convertView = inflater.inflate(R.layout.list_item_no_content, null, false);
            }
            return convertView;
        }else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_subject, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mSubjectNameTextView = (TextView) convertView.findViewById(R.id.subject_listview_item_text1);
                viewHolder.mAssesmentTypeTextView = (TextView) convertView.findViewById(R.id.subject_listview_item_text2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Triplet<String, String, Integer> item = getItem(position);
            viewHolder.mSubjectNameTextView.setTypeface(robotoFont);
            viewHolder.mSubjectNameTextView.setText(item.getValue0());
            viewHolder.mAssesmentTypeTextView.setTypeface(robotoFont);
            viewHolder.mAssesmentTypeTextView.setText(item.getValue1());

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView mSubjectNameTextView;
        TextView mAssesmentTypeTextView;
    }
}