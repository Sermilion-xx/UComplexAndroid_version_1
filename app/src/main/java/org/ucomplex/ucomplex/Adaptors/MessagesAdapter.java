package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Message;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sermilion on 31/12/2015.
 */
public class MessagesAdapter extends ArrayAdapter {

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

    protected ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Typeface robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");

    private static final int TYPE_OUT = 0;
    private static final int TYPE_IN = 1;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public MessagesAdapter(Context context, LinkedList<Message> messages, String companion, String name) {
        super(context, -1, messages);
        this.values = messages;
        if (values.size() > 0) {
            if (values.getFirst() instanceof Bitmap) {
                this.bitmap = (Bitmap) values.getFirst();
                values.removeFirst();
            }
        }
        this.companion = companion;
        this.companionName = name;
        this.context = context;
        User user = Common.getUserDataFromPref(context);
        person = user.getPerson();
        this.myName = user.getName();

        myBitmap = Common.decodePhotoPref(context, "profilePhoto");
        if (myBitmap == null) {
            drawable1 = createDrawable(this.myName, person);
        }
        if (this.bitmap == null) {
            this.drawable2 = createDrawable(companionName, Integer.valueOf(this.companion));
        }

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }

    public LinkedList getValues() {
        return values;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) getItem(position);
        return message.getFrom() == person ? TYPE_OUT : TYPE_IN;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    @Override
    public Object getItem(int position) {
        return this.values.get(position);
    }


    private View createConverterView(ViewHolder viewHolder, View convertView, int viewType, int position) {
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (viewType == TYPE_OUT) {
            convertView = inflater.inflate(R.layout.list_item_messages_right, null);
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_text);
            viewHolder.messageBitmap = (CircleImageView) convertView.findViewById(R.id.message_image);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_time);
            if (position > 0) {
                if (getItemViewType(position - 1) == viewType) {
                    viewHolder.messageTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bubble2_out));
                }
            }
            viewHolder.holderId = TYPE_OUT;
//            if(this.myBitmap == null) {
//                viewHolder.profileImageView.setImageDrawable(drawable1);
//            }else{
//                viewHolder.profileImageView.setImageBitmap(this.myBitmap);
//            }
        } else if (viewType == TYPE_IN) {
            convertView = inflater.inflate(R.layout.list_item_messages_left, null);
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_text);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_time);
            viewHolder.messageBitmap = (CircleImageView) convertView.findViewById(R.id.message_image);
//            if(this.bitmap == null){
//                viewHolder.profileImageView.setImageDrawable(drawable2);
//            }else{
//                viewHolder.profileImageView.setImageBitmap(this.bitmap);
//            }
            if (position > 0) {
                if (getItemViewType(position - 1) == viewType) {
                    viewHolder.messageTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bubble2_in));
                }
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
        ViewHolder viewHolder;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = createConverterView(null, null, viewType, position);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createConverterView(viewHolder, convertView, viewType, position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        final Message item = (Message) getItem(position);
        viewHolder.messageTextView.setTypeface(robotoFont);
        if(item.getMessage().length()==0 && item.getFiles().size()>0){
            StringBuilder sb = new StringBuilder();
            for(File file: item.getFiles()){
                sb.append(file.getName()+"\n");
            }
            viewHolder.messageTextView.setText(sb.toString());
        }else{
            viewHolder.messageTextView.setText(item.getMessage());
        }
        viewHolder.timeTextView.setTypeface(robotoFont);
        viewHolder.timeTextView.setText(item.getTime().split(" ")[1]);

        if(item.getFiles()!=null && item.getFiles().size()>0){
            File file = item.getFiles().get(0);

            String type = file.getAddress().split("\\.")[1];

            final ViewHolder finalViewHolder = viewHolder;
            if(item.getMessageImage()==null) {
                if (type.equals("jpg") || type.equals("png")) {
                    imageLoader.displayImage("http://storage.ucomplex.org/files/messages/" + ((Message) getItem(position)).getFiles().get(0).getFrom() + "/" + ((Message) getItem(position)).getFiles().get(0).getAddress(), viewHolder.messageBitmap, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {}
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            if (loadedImage != null) {
                                BitmapDrawable bitmapDrawable = ((BitmapDrawable) finalViewHolder.messageBitmap.getDrawable());
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                item.setMessageImage(bitmap);
                                if (bitmap != null) {
                                    finalViewHolder.messageBitmap.setImageBitmap(bitmap);
                                }
                            }
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        }
                    });
                } else {
                    finalViewHolder.messageBitmap.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attachment_dark));
                }
            }else{
                viewHolder.messageBitmap.setImageBitmap(item.getMessageImage());
            }
        }else{
            ViewGroup group = ((ViewGroup) viewHolder.messageBitmap.getParent());
            if(group!=null)
                group.removeView(viewHolder.messageBitmap);
        }
        return convertView;
    }

    private TextDrawable createDrawable(String name, int id) {
        TextDrawable drawable;
        int colorCount = 16;
        String firstLettet;
        if (name.split(" ").length > 1) {
            firstLettet = String.valueOf(name.split(" ")[1].charAt(0));
        } else {
            firstLettet = String.valueOf(name.split(" ")[0].charAt(0));
        }
        final int number = (id <= colorCount) ? id : id % colorCount;
        drawable = TextDrawable.builder().beginConfig()
                .width(60)
                .height(60)
                .endConfig()
                .buildRound(firstLettet, Common.getColor(number));
        return drawable;
    }

    public static class ViewHolder {
        int holderId;
        TextView messageTextView;
        TextView timeTextView;
        CircleImageView messageBitmap;


    }
}
