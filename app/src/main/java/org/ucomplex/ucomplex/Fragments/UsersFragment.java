package org.ucomplex.ucomplex.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.Tasks.FetchUsersTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.TreeSet;
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

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

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
            mItems = new FetchUsersTask(getActivity()).execute(usersType).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        boolean loaded = false;

//        Button btnLoadExtra = new Button(getContext());
//        btnLoadExtra.setText("Load More...");
//
//// Adding Load More button to lisview at bottom
//        getListView().addFooterView(btnLoadExtra);
//        imageAdapter = new ImageAdapter(getActivity(), mItems, false);
//        setListAdapter(imageAdapter);
//
//        btnLoadExtra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                // Starting a new async task
//                try {
//                    int lastPos = mItems.size();
//                    ArrayList<User> loadedUsers = new FetchUsersTask(getActivity()).execute(usersType,mItems.size()).get();
//                    boolean loaded = false;
//                    if(loadedUsers.size()<20){
//                        loaded = true;
//                    }
//                    mItems.addAll(loadedUsers);
//                    setListAdapter(new ImageAdapter(getContext(),mItems, loaded));
//                    getListView().setSelection(lastPos - 2);
//
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        btnLoadExtra = new Button(getContext());
        btnLoadExtra.setText("Load More...");
        if(mItems.size()<=20){
            btnLoadExtra.setVisibility(View.GONE);
        }

        getListView().addFooterView(btnLoadExtra);
        imageAdapter = new ImageAdapter(getActivity(), mItems, false);
        setListAdapter(imageAdapter);

        btnLoadExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                try {
                    int lastPos = mItems.size();
                    ArrayList<User> loadedUsers = new FetchUsersTask(getActivity()).execute(usersType,mItems.size()).get();
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

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        User item = mItems.get(position);
        Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();
    }


    private class ImageAdapter extends BaseAdapter {


		private LayoutInflater inflater;
		private DisplayImageOptions options;
        private boolean finishedLoading =false;
        private int counter = 0;
        ArrayList<User> mItems;
        private TreeSet mSeparatorsSet = new TreeSet();


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
//            if (!loaded) {
//                addSeparatorItem(new User());
//            }

		}

        public void addSeparatorItem(final User item) {
            this.mItems.add(new User());
            mSeparatorsSet.add(mItems.size()-1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
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
            int viewType = getItemViewType(position);

            if (convertView == null) {
                switch (viewType) {
                    case TYPE_SEPARATOR:
                        convertView = inflater.inflate(R.layout.list_users_item_loadmore, null);
                        break;
                    case TYPE_ITEM:
                        convertView = inflater.inflate(R.layout.list_users_item, null);
                        break;
                    default:
                }
                viewHolder = new ViewHolder(convertView, viewType);
                if (convertView != null) {
                    convertView.setTag(viewHolder);
                }

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(viewType!=TYPE_SEPARATOR){
                viewHolder.icon.setImageDrawable(getDrawable(position));
                if (!finishedLoading) {
                    ImageLoader.getInstance()
                            .displayImage("http://ucomplex.org/files/photos/" + mItems.get(position).getCode() + ".jpg", viewHolder.icon, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

//							holder.progressBar.setProgress(0);
//							holder.progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//							holder.progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//							holder.progressBar.setVisibility(View.GONE);
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

            switch (viewHolder.holderid) {
                case TYPE_SEPARATOR:
                    viewHolder.loadMoreIcon.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf"));
                    viewHolder.loadMoreIcon.setText("\uF021");
                    break;
                case TYPE_ITEM:
                    viewHolder.textView1.setText(user.getName());
                    viewHolder.textView2.setText(typeStr);
                    break;
            }
            return convertView;
        }

        public Drawable getDrawable(int position){
            final int colorsCount = 16;
            final int number = (getItem(position).getId() <= colorsCount) ? getItem(position).getId() : getItem(position).getId() % colorsCount;
            char firstLetter = getItem(position).getName().split("")[1].charAt(0);

            TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .width(60)  // width in px
                    .height(60) // height in px
                    .endConfig()
                    .buildRound(String.valueOf(firstLetter), Common.getColor(number));
            return drawable;
        }


	}

    static class ViewHolder {
        int holderid;
        ImageView icon;
        TextView loadMoreIcon;
        TextView textView1;
        TextView textView2;

        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            if (ViewType == TYPE_ITEM) {
                this.textView1 = (TextView) itemView.findViewById(R.id.list_users_item_textview1);
                this.textView2 = (TextView) itemView.findViewById(R.id.list_users_item_textview2);
                this.icon = (ImageView) itemView.findViewById(R.id.list_users_item_image);// Creating ImageView object with the id of ImageView from item_row.xml
                holderid = TYPE_ITEM;                                               // setting holder id as 1 as the object being populated are of type item row
            } else {
                this.loadMoreIcon = (TextView) itemView.findViewById(R.id.list_users_item_loadmore_icon);
                holderid = TYPE_SEPARATOR;
            }
        }

    }


}
