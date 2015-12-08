package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.R;

import java.util.List;

/**
 * Created by Sermilion on 07/12/2015.
 */
public class CourseMaterialsAdapter extends ArrayAdapter<File> {


    public CourseMaterialsAdapter(Context context, List<File> items){
        super(context, R.layout.course_material_listview_item,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.course_material_listview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.course_material_listview_item_textview1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.course_material_listview_item_textview2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File file = getItem(position);
        viewHolder.textView1.setText(file.getName());
        viewHolder.textView2.setText(file.getOwner().getName());

        return convertView;

    }

    public static class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}
