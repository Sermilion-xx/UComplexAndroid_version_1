package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Sermilion on 31/12/2015.
 */
public class MessagesAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    LinkedList values = new LinkedList();
    Bitmap bitmap;
    TextDrawable drawable1;
    TextDrawable drawable2;
    int person;
    String myName;
    Context context;
    Bitmap myBitmap;
    String companion;
    String companionName;

    private static final int TYPE_OUT = 0;
    private static final int TYPE_IN = 1;

    public MessagesAdapter(Context context, LinkedList<Message> messages, String companion, String name) {
        super(context, -1, messages);
        this.values = messages;
        if(values.size()>0){
            if(values.get(values.size()-1) instanceof Bitmap){
                this.bitmap = (Bitmap) values.get(values.size()-1);
                values.remove(values.size()-1);
            }
        }
        this.companion = companion;
        this.companionName = name;
        this.context = context;
        User user = Common.getUserDataFromPref(context);
        person = user.getPerson();
        this.myName = user.getName();

        myBitmap = Common.decodePhotoPref(context, "profilePhoto");
        if(myBitmap==null){
            drawable1 = createDrawable(this.myName, person);
        }
        if(this.bitmap == null){
            this.drawable2 = createDrawable(companionName, Integer.valueOf(this.companion));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) getItem(position);
        int  result;
        if(message.getFrom() == person)
            result = TYPE_OUT;
        else
            result= TYPE_IN;
        return result;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    @Override
    public Object getItem(int position) {
        return this.values.get(position);
    }


    private View createConverterView(ViewHolder viewHolder, View convertView, int viewType){
        viewHolder = new ViewHolder();
        inflater = LayoutInflater.from(getContext());
        if (viewType == TYPE_OUT) {
            convertView = inflater.inflate(R.layout.list_item_messages_right, null);
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_text);
            viewHolder.profileImageView = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.list_messages_message_right_image);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_time);
            viewHolder.holderId = TYPE_OUT;
            if(this.myBitmap == null) {
                viewHolder.profileImageView.setImageDrawable(drawable1);
            }else{
                viewHolder.profileImageView.setImageBitmap(this.myBitmap);
            }
        } else if (viewType == TYPE_IN) {
            convertView = inflater.inflate(R.layout.list_item_messages_left, null);
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_text);
            viewHolder.profileImageView = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.list_messages_message_left_image);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_time);
            if(this.bitmap == null){
                viewHolder.profileImageView.setImageDrawable(drawable2);
            }else{
                viewHolder.profileImageView.setImageBitmap(this.bitmap);
            }
            viewHolder.holderId = TYPE_IN;
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
            convertView = createConverterView(viewHolder, convertView, viewType);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if(viewHolder.holderId != viewType){
                convertView = createConverterView(viewHolder, convertView, viewType);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        Message item = (Message) getItem(position);
        viewHolder.messageTextView.setText(item.getMessage());
        viewHolder.timeTextView.setText(item.getTime().split(" ")[1]);
        return convertView;
    }

    private TextDrawable createDrawable(String name, int id){
        TextDrawable drawable;
        int colorCount = 16;
        final int number = (id <= colorCount) ? id : id % colorCount;
        drawable = TextDrawable.builder().beginConfig()
                .width(60)
                .height(60)
                .endConfig()
                .buildRound(String.valueOf(name.split(" ")[1].charAt(0)), Common.getColor(number));
        return drawable;
    }

    public static class ViewHolder {
        int holderId;
        TextView messageTextView;
        TextView timeTextView;
        de.hdodenhof.circleimageview.CircleImageView profileImageView;
    }
}
