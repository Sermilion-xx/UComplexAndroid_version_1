package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sermilion on 16/03/16.
 */
public class CalendarStatisticsAdapter extends ArrayAdapter<Quintet<String, String, Double, Double, Integer>> {
    private LayoutInflater inflater;
    ArrayList<Quintet<String, String, Double, Double, Integer>> values;
    ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItemsCurrent;
    ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItemsOld;
    private static final int TYPE_TITLE_1 = 0;
    private static final int TYPE_TITLE_2 = 1;
    private static final int TYPE_MARK = 2;
    Typeface robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");

    public CalendarStatisticsAdapter(Context context, ArrayList<Quintet<String, String, Double, Double, Integer>> values) {
        super(context, -1, values);
        statisticItemsCurrent = new ArrayList<>();
        statisticItemsOld = new ArrayList<>();

        statisticItemsCurrent.add(new Quintet<>("-1", "-1", -10.0, -10.0, -1));
        statisticItemsOld.add(new Quintet<>("-1", "-1", -10.0, -10.0, -2));
        this.values = new ArrayList<>();

        if(values!=null){
            for (Quintet<String, String, Double, Double, Integer> item : values) {
                if (item.getValue4() == 1) {
                    statisticItemsCurrent.add(item);
                } else if (item.getValue4() == 0) {
                    statisticItemsOld.add(item);
                }
            }
        }

        this.values.addAll(statisticItemsCurrent);
        this.values.addAll(statisticItemsOld);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getValue4()==-1) {
            return TYPE_TITLE_1;
        }if (getItem(position).getValue4()==-2) {
            return TYPE_TITLE_2;
        } else if (getItem(position).getValue4() == 1 || getItem(position).getValue4() == 0) {
            return TYPE_MARK;
        }
        return 0;
    }

    @Override
    public Quintet<String, String, Double, Double, Integer> getItem(int position) {
        return this.values.get(position);
    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType) {
        viewHolder = new ViewHolder();
        inflater = LayoutInflater.from(getContext());
        if (viewType == TYPE_TITLE_1) {
            convertView = inflater.inflate(R.layout.list_item_calendar_statistics_header, null);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_title);
            viewHolder.holderId = TYPE_TITLE_1;
        }else if (viewType == TYPE_TITLE_2) {
            convertView = inflater.inflate(R.layout.list_item_calendar_statistics_header, null);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_title);
            viewHolder.holderId = TYPE_TITLE_2;
        }
        else if (viewType == TYPE_MARK) {
            convertView = inflater.inflate(R.layout.list_item_calendar_statistics, null);
            viewHolder.subjectTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_subject_name);
            viewHolder.markTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_marks_value);
            viewHolder.attendanceTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_attendance_value);
            viewHolder.holderId = TYPE_MARK;
        }
        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = createHolder(viewHolder, convertView, viewType);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createHolder(viewHolder, convertView, viewType);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        Quintet<String, String, Double, Double, Integer> item = getItem(position);
        if(viewType==TYPE_MARK) {

            viewHolder.subjectTextView.setText(item.getValue1());
            viewHolder.markTextView.setText(String.valueOf(item.getValue2()));
            viewHolder.attendanceTextView.setText(String.valueOf(item.getValue3()));
        }else if(viewType==TYPE_TITLE_2){
            viewHolder.titleTextView.setText("Предыдушие семестры");
        }else if(viewType==TYPE_TITLE_1){
            viewHolder.titleTextView.setText("Текущий семестр");
        }

        return convertView;
    }


    public static class ViewHolder {
        int holderId;
        TextView attendanceTextView;
        TextView subjectTextView;
        TextView markTextView;
        TextView titleTextView;

        public ViewHolder() {
        }

    }
}
