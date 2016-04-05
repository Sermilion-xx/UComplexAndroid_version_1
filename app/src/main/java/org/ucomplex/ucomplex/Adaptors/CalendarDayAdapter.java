package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 25/12/2015.
 */
public class CalendarDayAdapter extends ArrayAdapter<Quintet<String,String,String,String, String>> {

    private LayoutInflater inflater;
    private final Context context;
    ArrayList values = new ArrayList();
    User user;
    Typeface robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");

    private static final int TYPE_MARK = 1;
    private static final int TYPE_SUBJECT = 3;

    //time, name, info, mark, color
    public CalendarDayAdapter(Context context, ArrayList<Quintet<String,String,String,String, String>> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        user = Common.getUserDataFromPref(context);
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_SUBJECT;
    }

    @Override
    public Quintet<String, String, String, String, String> getItem(int position) {
        return (Quintet<String, String, String, String, String>) this.values.get(position);
    }

    private View createHolder(ViewHolder viewHolder,View convertView, int viewType){
        viewHolder = new ViewHolder();
            if(viewType==TYPE_MARK ){
                convertView = inflater.inflate(R.layout.list_item_calendar_day_mark, null);
            viewHolder.subjectTextView = (TextView) convertView.findViewById(R.id.list_calendar_day_mark_subject);
            viewHolder.markTextView = (ImageView) convertView.findViewById(R.id.list_calendar_day_mark_mark);
            viewHolder.holderId=TYPE_MARK;

        }else if(viewType==TYPE_SUBJECT){
            convertView = inflater.inflate(R.layout.list_item_calendar_day_timetable, null);
            viewHolder.subjectTextView = (TextView) convertView.findViewById(R.id.list_calendar_day_subject);
            viewHolder.subjectTypeTextView = (TextView) convertView.findViewById(R.id.list_calendar_day_subject_type);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_calendar_day_subject_time);
            viewHolder.subjectTeacherTextView = (TextView) convertView.findViewById(R.id.list_calendar_day_subject_teacher);
            viewHolder.subjectRoomTextView = (TextView) convertView.findViewById(R.id.list_calendar_day_subject_room);
            viewHolder.holderId=TYPE_SUBJECT;
        }
        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return values.size()>0?values.size():1;
    }
    @Override
    public boolean isEnabled(int position) {
        return values.size() != 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        inflater = LayoutInflater.from(getContext());
        if (values.size()==0){
            if(!Common.isNetworkConnected(context)){
                convertView = inflater.inflate(R.layout.list_item_no_internet, null, false);
            }else{
                convertView = inflater.inflate(R.layout.list_item_no_content, null, false);
            }
            return convertView;
        }
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = createHolder(viewHolder, convertView, viewType);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if(viewHolder.holderId != viewType){
                convertView = createHolder(viewHolder, convertView, viewType);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        if(viewType==TYPE_MARK ){
            String color = getItem(position).getValue4();
            viewHolder.subjectTextView.setText(getItem(position).getValue1());
            TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .width(20)
                    .height(20)
                    .endConfig()
                    .buildRound(getLetter(Integer.parseInt(getItem(position).getValue3())), Integer.parseInt(color));
                viewHolder.markTextView.setImageDrawable(drawable);
        }else if(viewType==TYPE_SUBJECT){
            viewHolder.timeTextView.setTypeface(robotoFont);
            viewHolder.timeTextView.setText(getItem(position).getValue0());
            String[] subjectAndType = getItem(position).getValue1().split("/");
            viewHolder.subjectTextView.setTypeface(robotoFont);
            viewHolder.subjectTextView.setText(subjectAndType[0]);
            viewHolder.subjectTypeTextView.setTypeface(robotoFont);
            viewHolder.subjectTypeTextView.setText(Character.toUpperCase(subjectAndType[1].charAt(0)) + subjectAndType[1].substring(1));
            String[] info = getItem(position).getValue2().split(",");
            viewHolder.subjectTeacherTextView.setTypeface(robotoFont);
            viewHolder.subjectTeacherTextView.setText(info[0]);
            String roomNum = info[1].replaceAll("\\s+","");
            viewHolder.subjectRoomTextView.setTypeface(robotoFont);
            viewHolder.subjectRoomTextView.setText(Character.toUpperCase(roomNum.charAt(0)) + roomNum.substring(1));
        }

        return convertView;
    }

    @NonNull
    private static String getLetter(int mark){

        if(mark==-1){
            return "н";
        }else if(mark==0){
            return "✓";
        }else if(mark==-3){
            return "б";
        }else{
            return String.valueOf(mark);
        }
    }

    public static class ViewHolder {
        int holderId;
        TextView timeTextView;
        TextView subjectTextView;
        TextView subjectTypeTextView;
        TextView subjectTeacherTextView;
        TextView subjectRoomTextView;
        ImageView markTextView;

        public ViewHolder(){}

    }


}
