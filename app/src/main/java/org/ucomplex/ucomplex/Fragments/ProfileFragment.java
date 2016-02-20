package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchPersonTask;
import org.ucomplex.ucomplex.Adaptors.PersonAdapter;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 20/02/16.
 */
public class ProfileFragment extends ListFragment implements OnTaskCompleteListener {

    PersonAdapter mAdapter;
    Activity mContext;
    Bitmap mBitmap;
    int mPerson = -1;
    User mUser;

    public ProfileFragment() {

    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setPerson(int mPerson) {
        this.mPerson = mPerson;
    }

    public void setContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchPersonTask fetchPersonTask = new FetchPersonTask(getActivity(), this);
        fetchPersonTask.setPerson(String.valueOf(mPerson));
        fetchPersonTask.setmContext(mContext);
        fetchPersonTask.setupTask();
        setListAdapter(mAdapter);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        try {
            mUser = (User) task.get();
            if (mUser != null) {
                List<Triplet> items = new ArrayList<>();
                Triplet<Bitmap, User, String> item = new Triplet<>(mBitmap, mUser, "-1");
                items.add(item);
                for (User role : mUser.getRoles()) {
                    Triplet<String, String, String> aItem = new Triplet<>(String.valueOf(role.getType()), mUser.getPositionName(), "-1");
                    items.add(aItem);
                }
                PersonAdapter adapter = new PersonAdapter(mContext, items);
                getListView().setAdapter(adapter);

            } else {
                Toast.makeText(getActivity(), "Ошибка при загрузке пользователя!", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView =inflater.inflate(R.layout.fragment_profile, container, false);
//        mMessageButton = (Button) rootView.findViewById(R.id.list_profile_message_button);
//        mMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String companion;
//                String name;
//                Intent intent = new Intent(getContext(), MessagesActivity.class);
//                if(mUser.getPerson()==0){
//                    companion = String.valueOf(mUser.getId());
//                }else{
//                    companion = String.valueOf(mUser.getPerson());
//                }
//                name = String.valueOf(mUser.getName());
//                intent.putExtra("companion", companion);
//                intent.putExtra("name", name);
//                getContext().startActivity(intent);
//            }
//        });

//        mFriendButton = (Button) rootView.findViewById(R.id.list_profile_add_friend_button);
////        mBlacklistButton = (Button) rootView.findViewById(R.id.course_info_button_block);
//        mUserImageView = (ImageView) rootView.findViewById(R.id.list_profile_profile_picture);
//        mFirstNameView = (TextView) rootView.findViewById(R.id.list_profile_firstname);
//        mLastNameView = (TextView) rootView.findViewById(R.id.list_profile_lastname);
//        mRoleView = (TextView) rootView.findViewById(R.id.list_profile_role);
//    }
}
