package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.ucomplex.ucomplex.Common;
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
            viewHolder.weightTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_weight);
            viewHolder.weightIconTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_weight_icon);
            viewHolder.personIconTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_person_icon);
            viewHolder.timeIconTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_time_icon);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.course_material_listview_item_time);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.course_material_listview_item_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File file = getItem(position);
        if(file.getType().equals("f")){
            viewHolder.imageView.setImageResource(R.drawable.ic_folder);
        }else{
            viewHolder.imageView.setImageResource(R.mipmap.ic_file);
        }
        viewHolder.textView1.setText(file.getName());
        viewHolder.textView2.setText(file.getOwner().getName());
        viewHolder.weightIconTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf"));
        viewHolder.weightIconTextView.setText("\uF24E");
        viewHolder.weightTextView.setText(String.valueOf(Common.readableFileSize(file.getSize(), false)));
        viewHolder.personIconTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf"));
        viewHolder.personIconTextView.setText("\uF007");
        viewHolder.timeIconTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf"));
        viewHolder.timeIconTextView.setText("\uF017");
        viewHolder.timeTextView.setText(file.getTime());
        return convertView;

    }

    public static class ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView weightTextView;
        TextView weightIconTextView;
        TextView personIconTextView;
        TextView timeIconTextView;
        TextView timeTextView;
    }
}
