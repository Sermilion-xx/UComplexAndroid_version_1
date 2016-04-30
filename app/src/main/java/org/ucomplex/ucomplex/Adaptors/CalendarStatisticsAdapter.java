package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 16/03/16.
 */
public class CalendarStatisticsAdapter extends ArrayAdapter<Quintet<String, String, Double, Double, Integer>> {
    private LayoutInflater inflater;
    ArrayList<Quintet<String, String, Double, Double, Integer>> mItems;
    ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItemsCurrent;
    ArrayList<Quintet<String, String, Double, Double, Integer>> statisticItemsOld;
    private static final int TYPE_TITLE_1 = 0;
    private static final int TYPE_TITLE_2 = 1;
    private static final int TYPE_MARK = 2;

    public CalendarStatisticsAdapter(Context context, ArrayList<Quintet<String, String, Double, Double, Integer>> mItems) {
        super(context, -1, mItems);
        statisticItemsCurrent = new ArrayList<>();
        statisticItemsOld = new ArrayList<>();

        statisticItemsCurrent.add(new Quintet<>("-1", "-1", -10.0, -10.0, -1));
        statisticItemsOld.add(new Quintet<>("-1", "-1", -10.0, -10.0, -2));
        this.mItems = new ArrayList<>();

        for (Quintet<String, String, Double, Double, Integer> item : mItems) {
            if (item.getValue4() == 1) {
                statisticItemsCurrent.add(item);
            } else if (item.getValue4() == 0) {
                statisticItemsOld.add(item);
            }
        }
        double markAverage = 0;
        double attendanceAverage = 0;

        if (statisticItemsCurrent.size() > 1) {
            int markCount = 0;
            int attendanceCount = 0;
            for (int i = 1; i < statisticItemsCurrent.size(); i++) {
                if(statisticItemsCurrent.get(i).getValue2()>0){
                    markAverage += statisticItemsCurrent.get(i).getValue2();
                    markCount++;
                }
                if(statisticItemsCurrent.get(i).getValue3()>0){
                    attendanceAverage += statisticItemsCurrent.get(i).getValue3();
                    attendanceCount++;
                }
            }
            markAverage = Common.round(markAverage / markCount, 2);
            attendanceAverage = Common.round(attendanceAverage / attendanceCount,2);
            Quintet<String, String, Double, Double, Integer> markAverageItemCurrent = new Quintet<>("-1", "Итого", markAverage, attendanceAverage, 0);
            statisticItemsCurrent.add(markAverageItemCurrent);
        }


        this.mItems.addAll(statisticItemsCurrent);
        this.mItems.addAll(statisticItemsOld);
    }

    @Override
    public boolean isEnabled(int position) {
        return mItems.size() != 0;
    }

    @Override
    public int getCount() {
        return mItems.size() == 2 ? 1 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getValue4() == -1) {
            return TYPE_TITLE_1;
        }
        if (getItem(position).getValue4() == -2) {
            return TYPE_TITLE_2;
        } else if (getItem(position).getValue4() == 1 || getItem(position).getValue4() == 0) {
            return TYPE_MARK;
        }
        return 0;
    }

    @Override
    public Quintet<String, String, Double, Double, Integer> getItem(int position) {
        return this.mItems.get(position);
    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType) {
        viewHolder = new ViewHolder();
        if (viewType == TYPE_TITLE_1) {
            convertView = inflater.inflate(R.layout.list_item_calendar_statistics_header, null);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_title);
            viewHolder.holderId = TYPE_TITLE_1;
        } else if (viewType == TYPE_TITLE_2) {
            convertView = inflater.inflate(R.layout.list_item_calendar_statistics_header, null);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_title);
            viewHolder.holderId = TYPE_TITLE_2;
        } else if (viewType == TYPE_MARK) {
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
        inflater = LayoutInflater.from(getContext());
        if (mItems.size() == 2) {
            if (!Common.isNetworkConnected(getContext())) {
                convertView = inflater.inflate(R.layout.list_item_no_internet, null, false);
            } else {
                convertView = inflater.inflate(R.layout.list_item_no_content, null, false);
            }
            return convertView;
        }
        if (convertView != null && mItems.size() > 0) {
            convertView = null;
        }
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
        if (viewType == TYPE_MARK) {
            viewHolder.subjectTextView.setText(item.getValue1());
            viewHolder.markTextView.setText(String.valueOf(item.getValue2()));
            viewHolder.attendanceTextView.setText(String.valueOf(item.getValue3())+" %");
        } else if (viewType == TYPE_TITLE_2) {
            viewHolder.titleTextView.setText("Предыдущие семестры");
        } else if (viewType == TYPE_TITLE_1) {
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
