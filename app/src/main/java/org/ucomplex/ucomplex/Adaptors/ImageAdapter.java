package org.ucomplex.ucomplex.Adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.ucomplex.ucomplex.Activities.MessagesActivity;
import org.ucomplex.ucomplex.Activities.UsersActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 12/01/2016.
 */
public class ImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private DisplayImageOptions options;
    ArrayList<User> mItems;
    protected ImageLoader imageLoader;
    private int usersType;
    public Activity context;

    public ImageAdapter(ArrayList<User> items, Activity context, int usersType) {
        this.inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        this.mItems = items;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.usersType = usersType;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(mItems!=null){
            return mItems.size();
        }else
            return 0;
    }



    @Override
    public User getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_users, null);
            viewHolder = new ViewHolder(convertView, position, this, usersType, mItems, context);
            if (convertView != null) {
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.icon.setImageDrawable(getDrawable(position));
        if (mItems.get(position).getPhotoBitmap()==null && mItems.get(position).getPhoto()==1){
            imageLoader.displayImage("http://ucomplex.org/files/photos/" + mItems.get(position).getCode() + ".jpg", viewHolder.icon, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if(loadedImage!=null){
                        BitmapDrawable bitmapDrawable = ((BitmapDrawable) viewHolder.icon.getDrawable());
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        mItems.get(position).setPhotoBitmap(bitmap);
                        viewHolder.icon.setImageBitmap(bitmap);
                    }else{
                        viewHolder.icon.setImageDrawable(getDrawable(position));
                    }
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                }
            });

        } else {
            Bitmap image = mItems.get(position).getPhotoBitmap();
            if(image!=null)
                viewHolder.icon.setImageBitmap(getItem(position).getPhotoBitmap());
            else {
                viewHolder.icon.setImageDrawable(getDrawable(position));
            }
        }

        User user = getItem(position);
        int type = user.getType();
        String typeStr = null;
        if (type == 0) {
            typeStr = context.getResources().getString(R.string.sotrudnik);
        }if (type == 1) {
            typeStr = context.getResources().getString(R.string.administrator);
        }if (type == 2) {
            typeStr = context.getResources().getString(R.string.sub_administrator);
        }else if (type == 3) {
            typeStr = context.getResources().getString(R.string.prepodvatel);
        }else if (type == 4) {
            typeStr = context.getResources().getString(R.string.student);
        }else if (type == 5) {
            typeStr = context.getResources().getString(R.string.metodist_po_raspisaniyu);
        }else if (type == 6) {
            typeStr = context.getResources().getString(R.string.metodist_ko);
        }else if (type == 7) {
            typeStr = context.getResources().getString(R.string.bibliotekar);
        }else if (type == 8) {
            typeStr = context.getResources().getString(R.string.tehsekretar);
        }else if (type == 9) {
            typeStr = context.getResources().getString(R.string.abiturient);
        }else if (type == 10) {
            typeStr = context.getResources().getString(R.string.uchebny_otdel);
        }else if (type == 11) {
            typeStr = context.getResources().getString(R.string.rukovoditel);
        }else if (type == 12) {
            typeStr = context.getResources().getString(R.string.monitoring);
        }else if (type == 13) {
            typeStr = context.getResources().getString(R.string.dekan);
        }else if (type == 14) {
            typeStr = context.getResources().getString(R.string.otdel_kadrov);
        }

        viewHolder.textView1.setText(user.getName());
        viewHolder.textView2.setText(typeStr);
        return convertView;
    }

    public Drawable getDrawable(int position){
        final int colorsCount = 16;
        final int number = (getItem(position).getId() <= colorsCount) ? getItem(position).getId() : getItem(position).getId() % colorsCount;
        char firstLetter = getItem(position).getName().split("")[1].charAt(0);

        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .width(60)
                .height(60)
                .endConfig()
                .buildRound(String.valueOf(firstLetter), Common.getColor(number));
        return drawable;
    }

}

class ViewHolder {
    ImageView icon;
    TextView textView1;
    TextView textView2;
    Button btnMenu;
    ArrayList<String> actionsArrayList = new ArrayList<>();
    ImageAdapter imageAdapter;
    Activity context;


    public ViewHolder(View itemView, final int position, final ImageAdapter adapter, final int usersType, final ArrayList<User> mItems, final Activity context) {
        imageAdapter = adapter;
        this.textView1 = (TextView) itemView.findViewById(R.id.list_users_item_textview1);
        this.textView2 = (TextView) itemView.findViewById(R.id.list_users_item_textview2);
        this.icon      = (ImageView) itemView.findViewById(R.id.list_users_item_image);
        this.btnMenu        = (Button) itemView.findViewById(R.id.list_users_item_menu_button);
        this.context = context;

        this.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsArrayList.clear();
                switch (usersType){
                    case 0:
                        actionsArrayList.add("Написать сообщение");
                        actionsArrayList.add("Добавить в друзья");
                        break;
                    case 2:
                        actionsArrayList.add("Написать сообщение");
                        actionsArrayList.add("Добавить в друзья");
                        break;
                    case 3:
                        actionsArrayList.add("Написать сообщение");
                        actionsArrayList.add("Добавить в друзья");
                        break;
                    case 1:
                        actionsArrayList.add("Написать сообщение");
                        if(mItems.get(position).isFriendRequested()){
                            actionsArrayList.add("Принять заявку");
                        }else{
                            actionsArrayList.add("Удалить из друзей");
                        }
                        actionsArrayList.add("Заблокировать");
                        break;
                    case 4:
                        actionsArrayList.add("Удалить из списка");
                        break;
                    default:

                }
                AlertDialog.Builder build = new AlertDialog.Builder(context);
                build.setItems(actionsArrayList.toArray(new String[actionsArrayList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, String> params = new HashMap<>();
                        switch (which) {
                            case 0:
                                if (usersType == 0 || usersType == 1 || usersType == 2 || usersType == 3) {
                                    Intent intent = new Intent(context, MessagesActivity.class);
                                    String companion = String.valueOf(mItems.get(which).getPerson());
                                    String name = String.valueOf(mItems.get(which).getName());
                                    intent.putExtra("companion", companion);
                                    intent.putExtra("name", name);
                                    context.startActivity(intent);
                                }
                                else if (usersType == 4) {
                                    params.put("user", String.valueOf(mItems.get(position).getId()));
                                    HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                                    handleMenuPress1.execute("http://you.com.ru/user/blacklist/delete", params);
                                    mItems.remove(position);
                                    Toast.makeText(context, "Пользователь удален из черного списка :)", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if (usersType == 0 || usersType == 3 || usersType == 3) {
                                    params.put("user", String.valueOf(mItems.get(position).getId()));
                                    HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                                    handleMenuPress1.execute("http://you.com.ru/user/friends/add", params);
                                    Toast.makeText(context, "Заявка на дружбу отправлена :)", Toast.LENGTH_SHORT).show();
                                } else if (usersType == 1) {
                                    params.put("user", String.valueOf(mItems.get(position).getId()));
                                    if (mItems.get(position).isFriendRequested()) {
                                        HandleMenuPress handleMenuPress = new HandleMenuPress();
                                        handleMenuPress.execute("http://you.com.ru/user/friends/accept", params);
                                        mItems.get(position).setFriendRequested(false);
                                        Toast.makeText(context, mItems.get(position).getName() + " теперь ваш друг :)", Toast.LENGTH_SHORT).show();
                                    } else {
                                        HandleMenuPress handleMenuPress = new HandleMenuPress();
                                        handleMenuPress.execute("http://you.com.ru/user/friends/delete", params);
                                        mItems.remove(position);
                                        Toast.makeText(context, "Пользователь удален из друзей :(", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            case 2:
                                if(usersType!=4) {
                                    params.put("user", String.valueOf(mItems.get(position).getId()));
                                    HandleMenuPress handleMenuPress = new HandleMenuPress();
                                    handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
                                    mItems.remove(position);
                                    Toast.makeText(context, "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                        }
                        UsersActivity act = (UsersActivity)context;
                        ViewPager viewPager = (ViewPager) context.findViewById(R.id.users_viewpager);
                        act.setupViewPager(viewPager);
                        viewPager.setCurrentItem(usersType);
                    }
                }).create().show();
            }
        });
    }

    class HandleMenuPress extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Common.httpPost((String) params[0],Common.getLoginDataFromPref(context), (HashMap<String, String>) params[1]);
            return null;
        }
    }
}

