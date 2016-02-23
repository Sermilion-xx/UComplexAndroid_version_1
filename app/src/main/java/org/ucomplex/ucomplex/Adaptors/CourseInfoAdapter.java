package org.ucomplex.ucomplex.Adaptors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sermilion on 23/02/16.
 */
public class CourseInfoAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TEACHER = 1;
    private static final int TYPE_HEADER_2 = 2;
    private static final int TYPE_COURSE_INFO = 3;

    private LayoutInflater mInflater;
    private DisplayImageOptions mOptions;
    ArrayList<Triplet<String, String, String>> mItems;
    protected ImageLoader mImageLoader;
    Activity mContext;
    private int HEADER_2_POSITION = 1;


    public CourseInfoAdapter(ArrayList<Triplet<String, String, String>> items, Activity context) {
        this.mInflater = LayoutInflater.from(context);
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        this.mItems = items;
        for(Triplet triplet:mItems){
            if(!triplet.getValue2().equals("-1")){
                HEADER_2_POSITION++;
            }
        }
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.mContext = context;
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }else if(position == HEADER_2_POSITION){
            return TYPE_HEADER_2;
        }else if(mItems.get(position).getValue2().equals("-1")){
            return TYPE_COURSE_INFO;
        }else if(!mItems.get(position).getValue2().equals("-1")){
            return TYPE_TEACHER;
        }
        return -1;
    }

    @Override
    public int getCount() {
        if (mItems != null) {
            return mItems.size();
        } else
            return 0;
    }

    @Override
    public Triplet<String, String, String> getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View createConverterView(ViewHolder viewHolder, View convertView, int viewType) {
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_HEADER) {
            convertView = inflater.inflate(R.layout.list_item_course_header, null);
            viewHolder.holderId = TYPE_HEADER;
        } else if (viewType == TYPE_HEADER_2) {
            convertView = inflater.inflate(R.layout.list_item_course_header2, null);
            viewHolder.holderId = TYPE_HEADER_2;
        } else if (viewType == TYPE_TEACHER) {
            convertView = inflater.inflate(R.layout.list_item_course_teacher, null);
            viewHolder.teacherImageView = (CircleImageView) convertView.findViewById(R.id.course_teacher);
            viewHolder.teacherNameTextView = (TextView) convertView.findViewById(R.id.course_teacher_name);
            viewHolder.holderId = TYPE_TEACHER;
        } else if (viewType == TYPE_COURSE_INFO) {
            convertView = inflater.inflate(R.layout.list_item_course_info, null);
            viewHolder.markTextView = (TextView) convertView.findViewById(R.id.course_info_course_mark);
            viewHolder.attendanceTextView = (TextView) convertView.findViewById(R.id.course_info_course_attendance);
            viewHolder.holderId = TYPE_COURSE_INFO;
        }

        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = createConverterView(null, null, viewType);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createConverterView(viewHolder, convertView, viewType);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        Triplet<String, String, String> item = getItem(position);

        if(viewType == TYPE_TEACHER){
            viewHolder.teacherImageView.setImageDrawable(getDrawable(position));
            if (!mItems.get(position).getValue0().equals("-1")) {
                final ViewHolder finalViewHolder = viewHolder;
                mImageLoader.displayImage("http://ucomplex.org/files/photos/" + mItems.get(position).getValue0() + ".jpg", viewHolder.teacherImageView, mOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (loadedImage != null) {
                            BitmapDrawable bitmapDrawable = ((BitmapDrawable) finalViewHolder.teacherImageView.getDrawable());
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            finalViewHolder.teacherImageView.setImageBitmap(bitmap);
                        } else {
                            finalViewHolder.teacherImageView.setImageDrawable(getDrawable(position));
                        }
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {}
                });
            }
            viewHolder.teacherNameTextView.setText(item.getValue1());
        }else if(viewType == TYPE_COURSE_INFO){
            viewHolder.markTextView.setText(item.getValue1());
            viewHolder.attendanceTextView.setText(item.getValue2());
        }
        return convertView;
    }

    public Drawable getDrawable(int position) {
        final int colorsCount = 16;
        final int number = (Integer.valueOf(getItem(position).getValue0()) <= colorsCount) ? Integer.valueOf(getItem(position).getValue0()) : Integer.valueOf(getItem(position).getValue0()) % colorsCount;
        char firstLetter = getItem(position).getValue1().split("")[1].charAt(0);

        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .width(60)
                .height(60)
                .endConfig()
                .buildRound(String.valueOf(firstLetter), Common.getColor(number));
        return drawable;
    }

    private class ViewHolder {
        int holderId;

        CircleImageView teacherImageView;
        TextView teacherNameTextView;

        TextView markTextView;
        TextView attendanceTextView;

    }
}
