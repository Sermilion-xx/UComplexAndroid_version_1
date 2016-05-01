package org.ucomplex.ucomplex.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.ProfileActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchUsersTask;
import org.ucomplex.ucomplex.Adaptors.ImageAdapter;
import org.ucomplex.ucomplex.Adaptors.UsersGroupAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.UsersGroup;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 01/05/16.
 */
public class UsersGroupFragment extends ListFragment {

    protected ArrayList<UsersGroup> mUsersGroups = new ArrayList<>();
    boolean searchShowing;

    AlertDialog.Builder builderSingle;


    public UsersGroupFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if(searchShowing){
            if(builderSingle!=null){
                builderSingle.show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FetchUsersGroup fetchUsersGroup = new FetchUsersGroup();
        fetchUsersGroup.execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        if (Common.isNetworkConnected(getContext())) {
            new FetchUsersTask(getActivity()){

                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(getActivity(), "",
                            "Загрузка", true);
                }

                @Override
                protected ArrayList<User> doInBackground(Integer... params) {
                    String urlString = "https://ucomplex.org/teacher/ajax/group_students?mobile=1";
                    HashMap<String, String> postData = new HashMap<>();
                    postData.put("group",String.valueOf(params[0]));
                    String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(getContext()), postData);
                    if(jsonData != null) {
                        return getUserDataFromJson(jsonData, params[0]);
                    }
                    return new ArrayList<>(mUsersGroups.get(position).getId());
                }

                @Override
                protected void onPostExecute(ArrayList<User> users) {
                    super.onPostExecute(users);
                    progressDialog.dismiss();
                    builderSingle = new AlertDialog.Builder(getContext());
                    builderSingle.setTitle(mUsersGroups.get(position).getName());

                    final ImageAdapter imageAdapter = new ImageAdapter(users, getActivity(), 0);

                    builderSingle.setNegativeButton(
                            "Назад",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    searchShowing = false;
                                    dialog.dismiss();
                                }
                            });

                    builderSingle.setAdapter(
                            imageAdapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Common.isNetworkConnected(getContext())){
                                                User user = imageAdapter.getItem(which);
                                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("person", String.valueOf(user.getPerson()));
                                        if (user.getPhotoBitmap() != null) {
                                            intent.putExtra("bitmap", user.getPhotoBitmap());
                                        }
                                        extras.putString("hasPhoto", String.valueOf(user.getPhoto()));
                                        extras.putString("code", user.getCode());
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(getContext(), "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    builderSingle.show();
                    searchShowing = true;
                }

            }.execute(mUsersGroups.get(position).getId());

        } else {
            Toast.makeText(getContext(), "Проверте интернет соединение.", Toast.LENGTH_LONG).show();
        }
    }

    private class FetchUsersGroup extends AsyncTask<Void, Void, ArrayList<UsersGroup>> {

        @Override
        protected ArrayList<UsersGroup> doInBackground(Void... params) {
            String urlString = "https://ucomplex.org/teacher/ajax/my_groups?json";
            String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(UsersGroupFragment.this.getContext()));
            if (jsonData != null) {
                return getUserGroupsDataFromJson(jsonData);
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<UsersGroup> usersGroups) {
            super.onPostExecute(usersGroups);
            mUsersGroups = usersGroups;
            UsersGroupAdapter adapter = new UsersGroupAdapter(getContext(), usersGroups);
            setListAdapter(adapter);
        }

        private ArrayList<UsersGroup> getUserGroupsDataFromJson(String jsonData) {
            JSONObject usersGroupsJsonArray;
            ArrayList<UsersGroup> usersGroups = new ArrayList<>();
            try {
                usersGroupsJsonArray = new JSONObject(jsonData);
                JSONArray groupJson = usersGroupsJsonArray.getJSONArray("groups");
                for (int i = 0; i < groupJson.length(); i++) {
                    JSONObject object = groupJson.getJSONObject(i);
                    UsersGroup usersGroup = new UsersGroup();
                    usersGroup.setId(object.getInt("id"));
                    usersGroup.setYear(object.getInt("year"));
                    usersGroup.setName(object.getString("name"));
                    usersGroup.setMajor(object.getInt("major"));
                    usersGroup.setSpecialism(object.getInt("specialism"));
                    usersGroup.setSize(object.getInt("size"));
                    usersGroup.setStudy(object.getInt("study"));
                    usersGroup.setStatus(object.getInt("status"));
                    usersGroup.setClient(object.getInt("client"));
                    usersGroups.add(usersGroup);
                }
                return usersGroups;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

}
