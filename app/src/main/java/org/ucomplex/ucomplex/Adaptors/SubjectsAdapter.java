package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.R;

import java.util.List;

/**
 * Created by Sermilion on 10/12/2015.
 */
public class SubjectsAdapter extends ArrayAdapter<Triplet<String, String, Integer>> {

    public SubjectsAdapter(Context context, List<Triplet<String, String, Integer>> items) {
        super(context, R.layout.list_subject_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_subject_item, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.mSubjectNameTextView = (TextView) convertView.findViewById(R.id.subject_listview_item_text1);
            viewHolder.mAssesmentTypeTextView = (TextView) convertView.findViewById(R.id.subject_listview_item_text2);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Triplet<String, String, Integer> item = getItem(position);
        viewHolder.mSubjectNameTextView.setText(item.getValue0());
        viewHolder.mAssesmentTypeTextView.setText(item.getValue1());

        return convertView;
    }

    private static class ViewHolder {
        TextView mSubjectNameTextView;
        TextView mAssesmentTypeTextView;
    }
}