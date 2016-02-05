package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.PersonActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchUsersTask;
import org.ucomplex.ucomplex.Adaptors.ImageAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;


public class UsersFragment extends ListFragment {

    ArrayList<User> mItems = new ArrayList<>();
    int usersType;
    ImageAdapter imageAdapter;
    Button btnLoadExtra;
    ArrayList<User> loadedUsers;
    int lastPos;
    Activity activity;


    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public UsersFragment() {

    }



    public void setUsersType(int usersType) {
        this.usersType = usersType;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mItems", mItems);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        User user = mItems.get(position);
        Intent intent = new Intent(getContext(), PersonActivity.class);
        Bundle extras = new Bundle();
        if (user.getPerson() == 0) {
            extras.putString("person", String.valueOf(user.getId()));
        } else {
            extras.putString("person", String.valueOf(user.getPerson()));
        }
        if (user.getPhotoBitmap() != null) {
            intent.putExtra("bitmap", user.getPhotoBitmap());
        }
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            mItems = (ArrayList<User>) savedInstanceState.getSerializable("mItems");
        }
        if ((mItems != null ? mItems.size() : 0) == 0) {
            fetchUsers();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (usersType == Common.userListChanged) {
            fetchUsers();
            Common.userListChanged = -1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void fetchUsers() {
        new FetchUsersTask(getActivity()) {
            @Override
            protected void onPostExecute(ArrayList<User> users) {
                super.onPostExecute(users);
                mItems = users;
                setListAdapter(new ImageAdapter(mItems, activity, usersType));
            }
        }.execute(usersType);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        imageAdapter = new ImageAdapter(mItems, getActivity(), usersType);

        if (usersType != 3) {
            btnLoadExtra = new Button(getContext());
            btnLoadExtra.setFocusable(false);
            btnLoadExtra.setText("Загрузить еще...");
            btnLoadExtra.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    new FetchUsersTask(getActivity()) {
                        @Override
                        protected void onPostExecute(ArrayList<User> users) {
                            super.onPostExecute(users);
                            lastPos = mItems.size();
                            loadedUsers = users;
                            if (loadedUsers.size() <= 20) {
                                btnLoadExtra.setVisibility(View.GONE);
                            }
                            mItems.addAll(loadedUsers);
                            setListAdapter(new ImageAdapter(mItems, getActivity(), usersType));
                            getListView().setSelection(lastPos - 2);
                        }
                    }.execute(usersType);
                }
            });

            if (mItems != null) {
                if (mItems.size() <= 20) {
                    btnLoadExtra.setVisibility(View.GONE);
                }
                getListView().addFooterView(btnLoadExtra);
            } else {
                Toast.makeText(getActivity(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        }

        if (mItems.size() == 0) {
            fetchUsers();
        }
    }


}
