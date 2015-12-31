package org.ucomplex.ucomplex.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Activities.PersonActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchUsersTask;
import org.ucomplex.ucomplex.Activities.UsersActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MyServices;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.widget.BaseAdapter;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class UsersFragment extends ListFragment {

    ArrayList<User> mItems;
    int usersType;
    ImageAdapter imageAdapter;
    Button btnLoadExtra;
    private boolean isViewShown = false;


    public UsersFragment() {
        // Required empty public constructor
    }

    public void setUsersType(int usersType) {
        this.usersType = usersType;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mItems = new FetchUsersTask(getActivity(), imageAdapter).execute(usersType).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);

        if(usersType!=3) {
            btnLoadExtra = new Button(getContext());
            btnLoadExtra.setFocusable(false);
            btnLoadExtra.setText("Load More...");
            btnLoadExtra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Starting a new async task
                    try {
                        int lastPos = mItems.size();
                        ArrayList<User> loadedUsers = new FetchUsersTask(getActivity(),imageAdapter).execute(usersType,mItems.size()).get();
                        boolean loaded = false;
                        if(loadedUsers.size()<=20){
                            loaded = true;
                            btnLoadExtra.setVisibility(View.GONE);
                        }
                        mItems.addAll(loadedUsers);
                        setListAdapter(new ImageAdapter(getContext(),mItems, loaded));
                        getListView().setSelection(lastPos - 2);

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (mItems.size() <= 20) {
                btnLoadExtra.setVisibility(View.GONE);
            }

            getListView().addFooterView(btnLoadExtra);
        }
        imageAdapter = new ImageAdapter(getActivity(), mItems, false);
        setListAdapter(imageAdapter);


    }





    private class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private DisplayImageOptions options;
        private boolean finishedLoading =false;
        private int counter = 0;
        ArrayList<User> mItems;

		ImageAdapter(Context context, ArrayList<User> items,  boolean loaded) {
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
            this.mItems = items;
		}

        @Override
        public int getCount() {
            return mItems.size();
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
                viewHolder = new ViewHolder(convertView, position, this);
                if (convertView != null) {
                    convertView.setTag(viewHolder);
                }

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
                viewHolder.icon.setImageDrawable(getDrawable(position));
                if (!finishedLoading) {
                    ImageLoader.getInstance()
                            .displayImage("http://ucomplex.org/files/photos/" + mItems.get(position).getCode() + ".jpg", viewHolder.icon, options, new SimpleImageLoadingListener() {
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
//							holder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });

//                    if (counter < mItems.size()) {
//                        counter++;
//                    } else {
//                        finishedLoading = true;
//                    }

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
            if (type == 1) {
                typeStr = "Администратор";
            } else if (type == 2) {
                typeStr = "Преподаватель";
            } else if (type == 4) {
                typeStr = "Студент";
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

        public ViewHolder(View itemView, final int position, final ImageAdapter adapter) {
            imageAdapter = adapter;
                this.textView1 = (TextView) itemView.findViewById(R.id.list_users_item_textview1);
                this.textView2 = (TextView) itemView.findViewById(R.id.list_users_item_textview2);
                this.icon      = (ImageView) itemView.findViewById(R.id.list_users_item_image);
                this.btnMenu        = (Button) itemView.findViewById(R.id.list_users_item_menu_button);

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
                            actionsArrayList.add("Удалить из друзей");
                            break;
                        case 1:
                            actionsArrayList.add("Написать сообщение");
                            if(mItems.get(position).isFriendRequested()){
                                actionsArrayList.add("Добавить в друзья");
                            }else{
                                actionsArrayList.add("Удалить из друзей");
                            }
                            actionsArrayList.add("Заблокировать");
                            break;
                        case 4:
                            actionsArrayList.add("Удалить из списка");
                        default:
                            btnMenu.setVisibility(View.GONE);
                    }
                    AlertDialog.Builder build = new AlertDialog.Builder(getContext());
                    build.setItems(actionsArrayList.toArray(new String[actionsArrayList.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final HashMap<String, String> params = new HashMap<>();
                                switch (which){
                                    case 0:
                                        //write a message to friend
                                        if(usersType==1){

                                        }// Remove form blacklist
                                        else if(usersType==4){
                                            params.put("user", String.valueOf(mItems.get(position).getId()));
                                            HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                                            handleMenuPress1.execute("http://you.com.ru/user/blacklist/delete", params);
                                            mItems.remove(position);
                                            Toast.makeText(getActivity(), "Пользователь удален из черного списка :)", Toast.LENGTH_SHORT).show();

                                            //Add friend form online list
                                        }else if(usersType==0){
                                            params.put("user", String.valueOf(mItems.get(position).getId()));
                                            HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                                            handleMenuPress1.execute("http://you.com.ru/user/friends/add", params);
                                            Toast.makeText(getActivity(), "Заявка на дружбу отправлена :)", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 1:
                                        //Add/Remove friend request
                                        if(usersType==1){
                                            params.put("user", String.valueOf(mItems.get(position).getId()));
                                            if(mItems.get(position).isFriendRequested()) {
                                                HandleMenuPress handleMenuPress = new HandleMenuPress();
                                                handleMenuPress.execute("http://you.com.ru/user/friends/accept", params);
                                                mItems.get(position).setFriendRequested(false);
                                                Toast.makeText(getActivity(), mItems.get(position).getName() + " теперь ваш друг :)", Toast.LENGTH_SHORT).show();
                                            }else {
                                                HandleMenuPress handleMenuPress = new HandleMenuPress();
                                                handleMenuPress.execute("http://you.com.ru/user/friends/delete", params);
                                                mItems.remove(position);
                                                Toast.makeText(getActivity(), "Пользователь удален из друзей :(", Toast.LENGTH_SHORT).show();
                                            }

                                            //write a message to not friend
                                        }else if(usersType==0){

                                            //Add friend form group list
                                        }else if(usersType==2){
                                            params.put("user", String.valueOf(mItems.get(position).getId()));
                                            HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                                            handleMenuPress1.execute("http://you.com.ru/user/friends/add", params);
                                            Toast.makeText(getActivity(), "Заявка на дружбу отправлена :)", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 2:
                                        //add to blacklist
                                        if(usersType==1) {
                                            params.put("user", String.valueOf(mItems.get(position).getId()));
                                            HandleMenuPress handleMenuPress = new HandleMenuPress();
                                            handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
                                            mItems.remove(position);
                                            Toast.makeText(getActivity(), "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    case 4:
                                        break;
                                }
                            UsersActivity act = (UsersActivity)getActivity();
                            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.users_viewpager);
                            act.setupViewPager(viewPager);
                            viewPager.setCurrentItem(usersType);
                        }
                    }).create().show();
                }
            });
        }
    }

    class HandleMenuPress extends AsyncTask <Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Common.httpPost((String) params[0],MyServices.getLoginDataFromPref(getContext()), (HashMap<String, String>) params[1]);
            return null;
        }
    }

}
