package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sermilion on 20/02/16.
 */
public class PersonAdapter extends ArrayAdapter<Triplet> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_INFO = 1;

    private User           mUser;
    private Context        mContext;
    private List<Triplet>  mItems;
    private LayoutInflater inflater;
    private Typeface       robotoFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");

    public PersonAdapter(Context context, List<Triplet> profileItems) {
        super(context, R.layout.list_item_profile_header, profileItems);
        mUser = (User) profileItems.get(0).getValue1();
        mContext = context;
        mItems = profileItems;
    }

    private View createHolder(ViewHolder viewHolder, View convertView, int viewType, Context context, ViewGroup parent, int position) {
        viewHolder = new ViewHolder(context, this);
        inflater = LayoutInflater.from(getContext());

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_INFO;
        }
    }

    public class ViewHolder {
        int holderId;
        Button mMessageButton;
        Button mFriendButton;
        Button mBlacklistButton;

        ImageView mUserImageView;
        TextView  mFirstNameView;
        TextView  mLastNameView;
        TextView  mEmailView;

        TextView mInfoKey;
        TextView mInfoValue;




        public void setFriendButton(Button button, final int position) {
            this.mFriendButton = button;
            if(mUser.is_friend()){
                mFriendButton.setText("Удалить");
            }else{
                mFriendButton.setText("В друзья");
            }
            this.mFriendButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("user", String.valueOf(mUser.getId()));
                            if(mUser.isReq_sent()) {
                                HandleMenuPress handleMenuPress = new HandleMenuPress();
                                handleMenuPress.execute("http://you.com.ru/user/friends/accept?mobile=1", params);
                                mUser.setReq_sent(false);
                                Toast.makeText(mContext, mUser.getName() + " теперь ваш друг :)", Toast.LENGTH_SHORT).show();
                                mFriendButton.setText("Удалить");
                            }else {
                                HandleMenuPress handleMenuPress = new HandleMenuPress();
                                handleMenuPress.execute("http://you.com.ru/user/friends/delete?mobile=1", params);
                                Toast.makeText(mContext, "Пользователь удален из друзей :(", Toast.LENGTH_SHORT).show();
                                mUser.setReq_sent(false);
                                mFriendButton.setText("В друзья");
                                Common.userListChanged = 1;
                            }
                        }
                    });
        }

        public void setBlacklistButton(Button button, final int position){
            mBlacklistButton = button;
            if(mUser.isIs_black()){
                mBlacklistButton.setText("Разблокировать");
            }else{
                mBlacklistButton.setText("Зазблокировать");
            }
            mBlacklistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> params = new HashMap<>();
                    if(!mUser.isIs_black()) {
                        params.put("user", String.valueOf(mUser.getId()));
                        HandleMenuPress handleMenuPress = new HandleMenuPress();
                        handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
                        Toast.makeText(mContext, "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
                        mUser.setIs_black(true);
                        mBlacklistButton.setText("Разблокировать");
                    }else{
                        params.put("user", String.valueOf(mUser.getId()));
                        HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                        handleMenuPress1.execute("http://you.com.ru/user/blacklist/delete", params);
                        Toast.makeText(mContext, "Пользователь удален из черного списка :)", Toast.LENGTH_SHORT).show();
                        mUser.setIs_black(false);
                        mBlacklistButton.setText("Зазблокировать");
                    }
                    Common.userListChanged = 4;
                }
            });
        }

    }

    class HandleMenuPress extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Common.httpPost((String) params[0], Common.getLoginDataFromPref(getContext()), (HashMap<String, String>) params[1]);
            return null;
        }
    }
}
