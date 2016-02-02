package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Dialog;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 30/12/2015.
 */
public class MessagesListAdapter extends ArrayAdapter<Dialog> {

    private LayoutInflater inflater;
    ArrayList<Dialog> dialogs;
    private boolean finishedLoading = false;
    private DisplayImageOptions options;

    public MessagesListAdapter(Context context, ArrayList<Dialog> dialogs) {
        super(context, -1, dialogs);
        this.dialogs = dialogs;

        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public Dialog getItem(int position) {
        return dialogs.get(position);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            inflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_messages_user, null);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.list_messages_user_name);
            viewHolder.lastMessageTextView = (TextView) convertView.findViewById(R.id.list_messages_last_message);
            viewHolder.profileImageView = (ImageView) convertView.findViewById(R.id.list_messages_user_image);

            if (convertView != null) {
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Dialog item = getItem(position);

        if (item.getPhoto() == 1) {
            if (!finishedLoading) {
                final ViewHolder finalViewHolder = viewHolder;
                ImageLoader.getInstance()
                        .displayImage("http://ucomplex.org/files/photos/" + dialogs.get(position).getCode() + ".jpg", viewHolder.profileImageView, options, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if (loadedImage != null) {
                                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) finalViewHolder.profileImageView.getDrawable());
                                    Bitmap bitmap = bitmapDrawable.getBitmap();
                                    dialogs.get(position).setPhotoBitmap(bitmap);
                                    finalViewHolder.profileImageView.setImageBitmap(bitmap);
                                } else {
                                    finalViewHolder.profileImageView.setImageDrawable(getDrawable(position));
                                }
                            }
                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            }
                        });
            } else {
                Bitmap image = item.getPhotoBitmap();
                if (image != null)
                    viewHolder.profileImageView.setImageBitmap(getItem(position).getPhotoBitmap());
                else {
                    viewHolder.profileImageView.setImageDrawable(getDrawable(position));
                }
            }
        } else {
            viewHolder.profileImageView.setImageDrawable(getDrawable(position));
        }
        int newMessageFrom = dialogs.get(position).getFrom();
        if (Common.fromMessages.contains(newMessageFrom)) {
            convertView.setBackgroundColor(Color.parseColor("#ecfbfe"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        viewHolder.nameTextView.setText(item.getName());
        viewHolder.lastMessageTextView.setText(item.getMessage());
        return convertView;
    }


    public Drawable getDrawable(int position) {
        final int colorsCount = 16;
        int companion = getItem(position).getCompanion();
        final int number = (companion <= colorsCount) ? companion : companion % colorsCount;
        String name = getItem(position).getName();
        char firstLetter = name.split(" ")[1].charAt(0);

        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .width(60)
                .height(60)
                .endConfig()
                .buildRound(String.valueOf(firstLetter), Common.getColor(number));
        return drawable;
    }

    public static class ViewHolder {
        TextView nameTextView;
        TextView lastMessageTextView;
        ImageView profileImageView;
    }
}
