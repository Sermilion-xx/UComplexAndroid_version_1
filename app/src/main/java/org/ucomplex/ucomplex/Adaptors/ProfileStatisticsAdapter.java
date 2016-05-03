package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.R;
import java.util.ArrayList;

/**
 * Created by Sermilion on 08/04/16.
 */
public class ProfileStatisticsAdapter extends ArrayAdapter<Pair<String, String>> {
    private LayoutInflater inflater;
    ArrayList<Pair<String, String>> mItems = new ArrayList<>();
    Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_INFO = 1;

    public ProfileStatisticsAdapter(Context context, ArrayList<Pair<String, String>> items) {
        super(context, -1, items);
        this.mItems = new ArrayList<>();
        this.mItems = items;
        this.context = context;
    }

    @Override
    public boolean isEnabled(int position) {
        return mItems.size() != 0;
    }

    @Override
    public int getCount() {
        int count = mItems.size() == 0 ? 1 : mItems.size();
        if(mItems.size()==1){
            if(mItems.get(0).getValue0().equals("Студент") || mItems.get(0).getValue0().equals("Сотрудник")){
                mItems.add(new Pair<>("Профиль закрыт",""));
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_INFO;
        }
    }

    @Override
    public Pair<String, String> getItem(int position) {
        return this.mItems.get(position);
    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType) {
        viewHolder = new ViewHolder();
        if (viewType == TYPE_HEADER) {
            convertView = inflater.inflate(R.layout.list_item_calendar_statistics_header, null);
            viewHolder.headerTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_title);
            viewHolder.onlineTextView = (TextView) convertView.findViewById(R.id.calendar_statistics_online);
            viewHolder.holderId = TYPE_HEADER;
        } else if (viewType == TYPE_INFO) {
            convertView = inflater.inflate(R.layout.list_item_profile_statistics, null);
            viewHolder.keyTextView = (TextView) convertView.findViewById(R.id.profile_statistics_attendance_key);
            viewHolder.valueTextView = (TextView) convertView.findViewById(R.id.profile_statistics_attendance_value);
            viewHolder.holderId = TYPE_INFO;
        }
        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        int viewType = getItemViewType(position);
        inflater = LayoutInflater.from(getContext());
        if (mItems.size() == 0) {
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

        Pair<String, String> item = getItem(position);
        if (viewType == TYPE_HEADER) {
            viewHolder.headerTextView.setText(item.getValue0());
            if(!item.getValue1().equals(" ")){
                viewHolder.onlineTextView.setText(Common.makeDate(item.getValue1()));
            }
        } else if (viewType == TYPE_INFO) {
            viewHolder.keyTextView.setText(item.getValue0());
            viewHolder.valueTextView.setText(item.getValue1());
        }
        return convertView;
    }

    public static class ViewHolder {
        int holderId;
        TextView headerTextView;
        TextView keyTextView;
        TextView valueTextView;
        TextView onlineTextView;

        public ViewHolder() {
        }
    }
}
