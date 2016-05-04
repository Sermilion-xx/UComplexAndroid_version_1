package org.ucomplex.ucomplex.Adaptors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Activities.MessagesActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sermilion on 20/02/16.
 */
public class ProfileAdapter extends ArrayAdapter<Triplet> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_INFO = 1;
    private static final int TYPE_ACTIVITY = 2;

    private Bitmap mBitmap;
    private User mUser;
    private Context mContext;
    private List<Triplet> mItems;
    private LayoutInflater inflater;
    private Typeface robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    Drawable ic_remove_friend;
    Drawable ic_add_friend;
    User user;

    public ProfileAdapter(Context context, List<Triplet> profileItems, Bitmap bitmap, User user) {
        super(context, R.layout.list_item_profile_header, profileItems);
        mUser = (User) profileItems.get(0).getValue1();
        mContext = context;
        mBitmap = bitmap;
        mItems = profileItems;
        ic_add_friend = ContextCompat.getDrawable(mContext, R.drawable.ic_add_as_friend);
        ic_remove_friend = ContextCompat.getDrawable(mContext, R.drawable.ic_remove_friend);
        this.user = user;
    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType, ViewGroup parent, int posision) {
        viewHolder = new ViewHolder();
        inflater = LayoutInflater.from(getContext());
        if (viewType == TYPE_HEADER) {
            convertView = inflater.inflate(R.layout.list_item_profile_header, parent, false);
            viewHolder.mFriendButton = (Button) convertView.findViewById(R.id.list_profile_add_friend_button);
            viewHolder.setFriendButton();
//            viewHolder.mBlacklistButton = (Button) convertView.findViewById(R.id.course_info_button_block);
//            viewHolder.setBlacklistButton();
            viewHolder.mMessageButton = (Button) convertView.findViewById(R.id.list_profile_message_button);
            viewHolder.setMessageButton();
            viewHolder.mUserImageView = (CircleImageView) convertView.findViewById(R.id.list_profile_profile_picture);
            viewHolder.mFirstNameView = (TextView) convertView.findViewById(R.id.list_profile_firstname);
            viewHolder.mLastNameView = (TextView) convertView.findViewById(R.id.list_profile_lastname);
//            viewHolder.mEmailView = (TextView) convertView.findViewById(R.id.list_profile_role);
            viewHolder.mOnline = (CircleImageView) convertView.findViewById(R.id.list_profile_online);
            viewHolder.holderId = TYPE_HEADER;
        } else if (viewType == TYPE_INFO) {
            convertView = inflater.inflate(R.layout.list_item_profile_role, parent, false);
            viewHolder.mInfoKey = (TextView) convertView.findViewById(R.id.list_profile_role_key);
            viewHolder.mInfoValue = (TextView) convertView.findViewById(R.id.list_profile_role_value);
            viewHolder.mRole = (CircleImageView) convertView.findViewById(R.id.list_profile_role_icon);
            viewHolder.holderId = TYPE_INFO;
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
            convertView = createHolder(viewHolder, convertView, viewType, parent, position);
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.holderId != viewType) {
                convertView = createHolder(viewHolder, convertView, viewType, parent, position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        Triplet<String, String, String> item = getItem(position);
        if (viewType == TYPE_HEADER) {
            if (mBitmap == null) {
                Drawable drawable = Common.getDrawable((User) mItems.get(0).getValue1());
                viewHolder.mUserImageView.setImageDrawable(drawable);
            } else {
                viewHolder.mUserImageView.setImageBitmap(mBitmap);
            }
            viewHolder.mFirstNameView.setTypeface(robotoFont);
            String[] fullName = mUser.getName().split(" ");
            viewHolder.mFirstNameView.setText(fullName[0]);
            viewHolder.mLastNameView.setTypeface(robotoFont);
            if (fullName.length > 1) {
                viewHolder.mLastNameView.setText(fullName[1] + " " + fullName[2]);
            }

        } else if (viewType == TYPE_INFO) {
            Integer[] icons = new Integer[]{
                    R.drawable.ic_role_1,
                    R.drawable.ic_role_2,
                    R.drawable.ic_role_3,
                    R.drawable.ic_role_4,
                    R.drawable.ic_role_5};
            viewHolder.mRole.setImageResource(icons[position] - 1);
            viewHolder.mInfoKey.setTypeface(robotoFont);
            viewHolder.mInfoValue.setTypeface(robotoFont);

            int type;
            String[] typeTmp = item.getValue1().split("/");
            if (typeTmp.length > 1) {
                type = Integer.parseInt(typeTmp[1]);
                viewHolder.mInfoKey.setText(typeTmp[0]);
            } else {
                type = Integer.parseInt(typeTmp[0]);
                viewHolder.mInfoKey.setText(Common.getStringUserType(mContext, type));
            }
            if (type == 9)
                viewHolder.mInfoKey.setText("Абитуриент");
            if (type != 9 && type != 1)
                viewHolder.mInfoValue.setText(item.getValue0());
        }
        if (viewType == TYPE_HEADER) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        return convertView;
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
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Triplet getItem(int position) {
        return mItems.get(position);
    }

    public class ViewHolder {

        int holderId;
        Button mMessageButton;
        Button mFriendButton;
        Button mBlacklistButton;

        CircleImageView mUserImageView;
        CircleImageView mOnline;
        CircleImageView mRole;
        TextView mFirstNameView;
        TextView mLastNameView;

        TextView mInfoKey;
        TextView mInfoValue;

        public ViewHolder() {
        }

        public void setMessageButton() {
            if (mUser != null) {
                if (mUser.getPerson() == user.getPerson()) {
                    mMessageButton.setEnabled(false);
                    mMessageButton.setTextColor(ContextCompat.getColor(mContext, R.color.uc_gray_text_events));
                }
                mMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String companion;
                        String name;
                        Intent intent = new Intent(getContext(), MessagesActivity.class);
                        companion = String.valueOf(mUser.getPerson());
                        name = String.valueOf(mUser.getName());
                        intent.putExtra("companion", companion);
                        intent.putExtra("name", name);
                        intent.putExtra("profileImage", mBitmap);
                        getContext().startActivity(intent);
                    }
                });
            }
        }

        public void setFriendButton() {
            if (mUser != null) {

                if (mUser.getPerson() == user.getPerson()) {
                    mFriendButton.setEnabled(false);
                    mFriendButton.setTextColor(ContextCompat.getColor(mContext, R.color.uc_gray_text_events));
                } else if (mUser.is_friend()) {
                    mFriendButton.setText("Удалить");
                    mFriendButton.setCompoundDrawablesWithIntrinsicBounds(ic_remove_friend, null, null, null);
                } else {
                    mFriendButton.setText("В друзья");
                    mFriendButton.setCompoundDrawablesWithIntrinsicBounds(ic_add_friend, null, null, null);
                }
                this.mFriendButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (Common.isNetworkConnected(mContext)) {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("user", String.valueOf(mUser.getPerson()));
                                    if (!mUser.isReq_sent() && !mUser.is_friend()) {
                                        HandleMenuPress handleMenuPress = new HandleMenuPress();
                                        handleMenuPress.execute("http://you.com.ru/user/friends/accept?mobile=1", params);
                                        mUser.setReq_sent(false);
                                        Toast.makeText(mContext, mUser.getName() + " получил вашу заявку на дружбу :)", Toast.LENGTH_SHORT).show();
                                        mFriendButton.setText("В друзья");
                                        mFriendButton.setCompoundDrawablesWithIntrinsicBounds(ic_add_friend, null, null, null);
                                        mFriendButton.setTextColor(ContextCompat.getColor(mContext, R.color.uc_gray_text));
                                        mFriendButton.setEnabled(false);
                                    } else if (mUser.isReq_sent() && !mUser.is_friend()) {
                                        mFriendButton.setTextColor(ContextCompat.getColor(mContext, R.color.uc_gray_text));
                                        mFriendButton.setEnabled(false);
                                        Toast.makeText(mContext, "Заявка уже отправленна.", Toast.LENGTH_SHORT).show();
                                    } else if (mUser.is_friend()) {
                                        HandleMenuPress handleMenuPress = new HandleMenuPress();
                                        handleMenuPress.execute("http://you.com.ru/user/friends/delete?mobile=1", params);
                                        Toast.makeText(mContext, "Пользователь удален из друзей :(", Toast.LENGTH_SHORT).show();
                                        mUser.setIs_friend(false);
                                        mFriendButton.setText("В друзья");
                                        mFriendButton.setCompoundDrawablesWithIntrinsicBounds(ic_add_friend, null, null, null);
                                        Common.userListChanged = 1;
                                    }
                                } else {
                                    Toast.makeText(mContext, "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }
    }

    public void blockUser() {
        if (Common.isNetworkConnected(mContext)) {
            HashMap<String, String> params = new HashMap<>();
            if (!mUser.isIs_black()) {
                params.put("user", String.valueOf(mUser.getPerson()));
                HandleMenuPress handleMenuPress = new HandleMenuPress();
                handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
                Toast.makeText(mContext, "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
                mUser.setIs_black(true);
            } else {
                params.put("user", String.valueOf(mUser.getPerson()));
                HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                handleMenuPress1.execute("http://you.com.ru/user/blacklist/delete", params);
                Toast.makeText(mContext, "Пользователь удален из черного списка :)", Toast.LENGTH_SHORT).show();
                mUser.setIs_black(false);
            }
            Common.userListChanged = 4;
        } else {
            Toast.makeText(mContext, "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
        }
    }


//    public void setBlacklistButton(){
//        if(mUser.isIs_black()){
//            mBlacklistButton.setText("Разблокировать");
//        }else{
//            mBlacklistButton.setText("Зазблокировать");
//        }
//        mBlacklistButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String, String> params = new HashMap<>();
//                if(!mUser.isIs_black()) {
//                    params.put("user", String.valueOf(mUser.getId()));
//                    HandleMenuPress handleMenuPress = new HandleMenuPress();
//                    handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
//                    Toast.makeText(mContext, "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
//                    mUser.setIs_black(true);
//                    mBlacklistButton.setText("Разблокировать");
//                }else{
//                    params.put("user", String.valueOf(mUser.getId()));
//                    HandleMenuPress handleMenuPress1 = new HandleMenuPress();
//                    handleMenuPress1.execute("http://you.com.ru/user/blacklist/delete", params);
//                    Toast.makeText(mContext, "Пользователь удален из черного списка :)", Toast.LENGTH_SHORT).show();
//                    mUser.setIs_black(false);
//                    mBlacklistButton.setText("Зазблокировать");
//                }
//                Common.userListChanged = 4;
//            }
//        });
//    }

    class HandleMenuPress extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Common.httpPost((String) params[0], Common.getLoginDataFromPref(getContext()), (HashMap<String, String>) params[1]);
            return null;
        }
    }
}
