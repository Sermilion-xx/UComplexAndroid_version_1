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
import android.widget.ListView;
import android.widget.ProgressBar;
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
        if (!(getItem(position) instanceof Bitmap)) {
            Message message = (Message) getItem(position);
            return message.getFrom() == person ? TYPE_OUT : TYPE_IN;
        } else {
            if (values.getFirst() instanceof Bitmap) {
                this.bitmap = (Bitmap) values.getFirst();
                values.removeFirst();
            }
            Message message = (Message) getItem(position);
            return message.getFrom() == person ? TYPE_OUT : TYPE_IN;
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


    private View createConverterView(ViewHolder viewHolder, View convertView, int viewType, int position, Message msg) {
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (viewType == TYPE_OUT) {
            if (msg.getFiles() != null && msg.getFiles().size() > 0) {
                convertView = inflater.inflate(R.layout.list_item_messages_right, null);
                viewHolder.messageBitmap = (CircleImageView) convertView.findViewById(R.id.message_image);
                viewHolder.fileProgressBar = (ProgressBar) convertView.findViewById(R.id.imagep_rogress_right);
                viewHolder.fileProgressBar.setVisibility(View.INVISIBLE);
            } else {
                convertView = inflater.inflate(R.layout.list_item_messages_right_no_image, null);
            }
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_text);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_right_time);
            viewHolder.holderId = TYPE_OUT;
        } else if (viewType == TYPE_IN) {
            if (msg.getFiles() != null && msg.getFiles().size() > 0) {
                convertView = inflater.inflate(R.layout.list_item_messages_left, null);
                viewHolder.messageBitmap = (CircleImageView) convertView.findViewById(R.id.message_image);
                viewHolder.fileProgressBar = (ProgressBar) convertView.findViewById(R.id.imagep_rogress_left);
                viewHolder.fileProgressBar.setVisibility(View.INVISIBLE);
            } else {
                convertView = inflater.inflate(R.layout.list_item_messages_left_no_image, null);
            }
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_text);
            viewHolder.timeTextView = (TextView) convertView.findViewById(R.id.list_messages_message_left_time);
            viewHolder.holderId = TYPE_IN;
        }

        if (convertView != null) {
            convertView.setTag(viewHolder);
        }
        return convertView;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Message item = (Message) getItem(position);
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = createConverterView(viewHolder, convertView, viewType, position, item);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createConverterView(viewHolder, convertView, viewType, position, item);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        viewHolder.messageTextView.setTypeface(robotoFont);
        viewHolder.messageTextView.setText(item.getMessage());
        viewHolder.timeTextView.setTypeface(robotoFont);
        viewHolder.timeTextView.setText(item.getTime().split(" ")[1]);
        if(item.getFiles()!=null &&  item.getFiles().size()>0){
            String[] typeStr = item.getFiles().get(0).getName().split("\\.");
            String type = "";
            if(typeStr.length>1){
                type = typeStr[typeStr.length-1];
            }
            if(type.equals("jpg") || type.equals("png")){
                loadImage(item, viewHolder);
            }
        }


//        if (item.getFiles() != null && item.getFiles().size() > 0) {
//            File file = item.getFiles().get(0);
//            String type = file.getAddress().split("\\.")[1];
//
//            if (item.getMessageImage() == null) {
//                if (type.equals("jpg") || type.equals("png")) {
//                    loadImage(item, viewHolder);
//                } else {
//                    viewHolder.messageBitmap.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attachment_dark));
//                }
//            } else if (item.getFiles() != null && item.getFiles().size() > 0) {
//                if (item.getMessageImage() == null) {
//                    viewHolder.messageBitmap.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_image));
//                } else {
//                    viewHolder.messageBitmap.setImageBitmap(item.getMessageImage());
//                }
////                if (item.getMessage().length() == 0 && item.getFiles().size() > 0) {
////                    StringBuilder sb = new StringBuilder();
////                    for (File file1 : item.getFiles()) {
////                        sb.append(file1.getName() + "\n");
////                        item.setMessage(sb.toString());
////                    }
////                }
//                viewHolder.messageTextView.setText(item.getMessage());
//            }
//        }
        return convertView;
    }

    private void loadImage(final Message item, final ViewHolder viewHolder) {
        if(imageLoader == null){
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }
        imageLoader.displayImage("http://storage.ucomplex.org/files/messages/" + item.getFiles().get(0).getFrom() + "/" + item.getFiles().get(0).getAddress(),
                viewHolder.messageBitmap,
                options,
                new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    item.setMessageImage(loadedImage);
                    viewHolder.messageBitmap.setImageBitmap(loadedImage);

                    if (item.getMessage().length() == 0 && item.getFiles().size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (File file : item.getFiles()) {
                            sb.append(file.getName() + "\n");
                            item.setMessage(sb.toString());
                        }
                        viewHolder.messageTextView.setText(sb.toString());
                    }
                }
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
            }
        });
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
        ProgressBar fileProgressBar;

        public ProgressBar getFileProgressBar() {
            return fileProgressBar;
        }
    }
}
