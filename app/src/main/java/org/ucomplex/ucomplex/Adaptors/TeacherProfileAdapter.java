package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 04/05/16.
 */
public class TeacherProfileAdapter extends ArrayAdapter<Pair<String, String>> {

    private final static int TYPE_ACTYVITY = 0;
    private final static int TYPE_INFO = 1;

    private LayoutInflater inflater;

    private ArrayList<Pair<String, String>> mItems = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();

    public TeacherProfileAdapter(Context context, ArrayList<Pair<String, String>> list) {
        super(context, -1, list);
        mItems = list;
        colors.add("#E77272");
        colors.add("#E77D72");
        colors.add("#E78D72");
        colors.add("#E7A472");
        colors.add("#E8B472");
        colors.add("#E8C272");
        colors.add("#E8C272");
        colors.add("#E6E773");
        colors.add("#c3e874");
        colors.add("#89e874");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ACTYVITY;
        } else {
            return TYPE_INFO;
        }
    }

    @Override
    public Pair<String, String> getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    private View createHolder(int viewType, int position) {
        ViewHolder viewHolder = new ViewHolder();
        View convertView = null;
        if (viewType == TYPE_ACTYVITY) {
            convertView = inflater.inflate(R.layout.list_item_teacher_activity, null);
            viewHolder.viewDown = convertView.findViewById(R.id.list_item_rating_down);
            viewHolder.viewUp = convertView.findViewById(R.id.list_item_rating_up);
            viewHolder.keyTextView = (TextView) convertView.findViewById(R.id.list_tem_teacher_activity_key);
            viewHolder.valueTextView = (TextView) convertView.findViewById(R.id.list_tem_teacher_activity_value);
            viewHolder.holderId = TYPE_ACTYVITY;

        } else if (viewType == TYPE_INFO) {
            convertView = inflater.inflate(R.layout.list_item_subject, null);
            viewHolder.keyTextView = (TextView) convertView.findViewById(R.id.subject_listview_item_text1);
            viewHolder.valueTextView = (TextView) convertView.findViewById(R.id.subject_listview_item_text2);
            viewHolder.holderId = TYPE_INFO;
        }
        viewHolder.keyTextView.setTextSize(18);
        viewHolder.valueTextView.setTextSize(16);
        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        inflater = LayoutInflater.from(getContext());
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = createHolder(viewType, position);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createHolder(viewType, position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        Pair<String, String> item = getItem(position);
        if (viewHolder.holderId == TYPE_ACTYVITY) {
            int fullWidth = viewHolder.viewDown.getLayoutParams().width;
            int percent = Math.round(Float.valueOf(item.getValue1()));
            int width = (Math.round(Float.valueOf(item.getValue1()))*fullWidth / 100);
//            float density = getContext().getResources().getDisplayMetrics().density;
            if(percent <= 10){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(0)));
            }else if(percent<=20 && percent>10){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(1)));
            }else if(percent<=30 && percent>20){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(2)));
            }else if(percent<=40 && percent>30){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(3)));
            }else if(percent<=50 && percent>40){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(4)));
            }else if(percent<=60 && percent>50){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(5)));
            }else if(percent<=70 && percent>60){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(6)));
            }else if(percent<=80 && percent>70){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(7)));
            }else if(percent<=90 && percent>80){
                viewHolder.viewUp.setBackgroundColor(Color.parseColor(colors.get(8)));
            }
            viewHolder.viewUp.getLayoutParams().width = width;
            viewHolder.valueTextView.setText(Math.round(Float.valueOf(item.getValue1()))+" %");
        } else {
            viewHolder.keyTextView.setText(item.getValue0());
            viewHolder.valueTextView.setText(item.getValue1());
        }

        return convertView;
    }

    public class ViewHolder {
        int holderId;
        View viewUp;
        View viewDown;
        TextView keyTextView;
        TextView valueTextView;

        public ViewHolder() {

        }
    }

}
