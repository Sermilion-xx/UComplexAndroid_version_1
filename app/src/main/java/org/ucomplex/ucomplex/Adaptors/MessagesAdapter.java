package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

/**
 * Created by Sermilion on 31/12/2015.
 */
public class MessagesAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    private final Context context;
    ArrayList values = new ArrayList();
    Bitmap bitmap;
    TextDrawable drawable;
    User user;
    int person;

    private static final int TYPE_OUT = 0;
    private static final int TYPE_IN = 1;

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, -1, messages);
        this.context = context;
        this.values = messages;
        if(values.get(values.size()-1) instanceof Bitmap){
            this.bitmap = (Bitmap) values.get(values.size());
        }
        user = Common.getUserDataFromPref(context);
        person = user.getPerson();
        user = null;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) getItem(position);
        if(message.getFrom() == person){
            return TYPE_OUT;
        }else{
            return TYPE_IN;
        }
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    @Override
    public Object getItem(int position) {
        return this.values.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int viewType = getItemViewType(position);
        if(convertView == null) {
            if(inflater==null){
                inflater = LayoutInflater.from(getContext());
            }
            viewHolder = new ViewHolder();
            if (viewType == TYPE_OUT) {
                convertView = inflater.inflate(R.layout.list_item_messages_right, null);
                viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_text);
                viewHolder.profileImageView = (ImageView) convertView.findViewById(R.id.list_messages_message_right_image);
                viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_time);
                viewHolder.holderId = TYPE_OUT;
            } else if (viewType == TYPE_IN) {
                convertView = inflater.inflate(R.layout.list_item_messages_left, null);
                viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_text);
                viewHolder.profileImageView = (ImageView) convertView.findViewById(R.id.list_messages_message_left_image);
                viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_time);
                viewHolder.holderId = TYPE_IN;
            }
            if (convertView != null) {
                convertView.setTag(viewHolder);
            }
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message item = (Message) getItem(position);

        if(this.bitmap == null){
            if(drawable == null){
                TextDrawable drawable;
                String name = item.getName();
                int colorCount = 16;
                final int number = (item.getFrom() <= colorCount) ? item.getFrom() : item.getFrom() % colorCount;
                this.drawable = TextDrawable.builder().beginConfig()
                        .width(60)
                        .height(60)
                        .endConfig()
                        .buildRound(String.valueOf(name.split(" ")[1].charAt(0)), Common.getColor(number));
                viewHolder.profileImageView.setImageDrawable(this.drawable);
            }else{
                viewHolder.profileImageView.setImageDrawable(drawable);
            }
        }else{
            viewHolder.profileImageView.setImageBitmap(this.bitmap);
        }
        viewHolder.messageTextView.setText(item.getMessage());
        viewHolder.timeTextView.setText(item.getTime().split(" ")[1]);
        return convertView;
    }

    public static class ViewHolder {
        int holderId;
        TextView messageTextView;
        TextView timeTextView;
        ImageView profileImageView;
    }
}
