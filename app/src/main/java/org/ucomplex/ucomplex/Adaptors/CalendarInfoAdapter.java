package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Sermilion on 03/02/16.
 */
public class CalendarInfoAdapter extends ArrayAdapter<String> {

    private Integer[] images;

    public CalendarInfoAdapter(Context context, String[] items, Integer[] images) {
        super(context, android.R.layout.select_dialog_item, items);
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setCompoundDrawablesWithIntrinsicBounds(images[position], 0, 0, 0);
        textView.setTextSize(20);
        textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 150 /* this is item height */));
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics()));
        return view;
    }

}
