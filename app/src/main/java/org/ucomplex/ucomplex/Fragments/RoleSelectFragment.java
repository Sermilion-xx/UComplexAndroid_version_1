package org.ucomplex.ucomplex.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ucomplex.ucomplex.Activities.EventsActivity;
import org.ucomplex.ucomplex.Activities.UsersActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sermilion on 27/01/2016.
 */
public class RoleSelectFragment extends Fragment {

    User user = null;
    private RecyclerView mRoleRecyclerView;
    private RoleAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = Common.getUserDataFromPref(getContext());
        View view = inflater.inflate(R.layout.fragment_role_list, container, false);
        mRoleRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mRoleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        mAdapter = new RoleAdapter(user.getRoles());
        mRoleRecyclerView.setAdapter(mAdapter);
    }


    private class RoleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private User mUser;
        private int[] userIcons = {R.drawable.role_select_1,
                R.drawable.role_select_2,
                R.drawable.role_select_3,
                R.drawable.role_select_4,
                R.drawable.role_select_5};

        private CircleImageView mRoleImage;
        private TextView mRoleTextView;
        private int position;

        public RoleHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRoleImage = (CircleImageView) itemView.findViewById(R.id.role_select_image);
            mRoleTextView = (TextView) itemView.findViewById(R.id.role_select_role);
        }

        public void bindRole(User user, int position) {
            mUser = user;
            this.position = position;
            int drawable = -1;
            if (position < 5) {
                drawable = userIcons[position];
            } else {
                drawable = userIcons[position % userIcons.length];
            }
            if (mUser.getType() == 3) {
                mRoleTextView.setText("Преподаватель");
            } else if (user.getType() == 4) {
                mRoleTextView.setText("Студент");
            } else if (user.getType() == 0) {
                mRoleTextView.setText("Сотрудник");
            } else if (user.getType() == 3) {
                mRoleTextView.setText("Преподаватель");
            }
            mRoleImage.setImageResource(drawable);
        }

        @Override
        public void onClick(View v) {
            User user = Common.getUserDataFromPref(getContext());
            user.setType(mUser.getType());
            user.setId(mUser.getPerson());
            Common.setUserDataToPref(getContext(), user);
            user = null;
            Common.ROLE = mUser.getType();
            Common.setRoleToPref(getContext(), mUser.getType());
            Intent intent = new Intent(getContext(), EventsActivity.class);
            startActivity(intent);

        }
    }

    private class RoleAdapter extends RecyclerView.Adapter<RoleHolder> {

        private List<User> mRoles;

        public RoleAdapter(List<User> roles) {
            mRoles = roles;
        }

        @Override
        public RoleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_role, parent, false);
            return new RoleHolder(view);
        }

        @Override
        public void onBindViewHolder(RoleHolder holder, int position) {
            User role = mRoles.get(position);
            Common.ROLE = mRoles.get(position).getType();
            holder.bindRole(role, position);
        }

        @Override
        public int getItemCount() {
            return mRoles.size();
        }
    }

}
