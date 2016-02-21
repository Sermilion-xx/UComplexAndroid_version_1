package org.ucomplex.ucomplex.Activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.quinny898.library.persistentsearch.SearchBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Adaptors.ImageAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.UsersFragment;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;


public class UsersActivity extends AppCompatActivity {

    ViewPager pager;
    private PagerSlidingTabStrip tabs;
    private ViewPagerAdapter adapter;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            runSearch(query);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        toolbar.setTitle("Пользователи");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        handleIntent(getIntent());

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.users_viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        tabs.setTextColor(Color.WHITE);
        tabs.setIndicatorColor(Color.WHITE);
        tabs.setIndicatorHeight(8);
        tabs.setUnderlineHeight(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setupViewPager(ViewPager viewPager) {

        UsersFragment onlineUsersFragment = new UsersFragment();
        onlineUsersFragment.setUsersType(0);
        onlineUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(onlineUsersFragment, "Онлайн");

        UsersFragment friendsUsersFragment = new UsersFragment();
        friendsUsersFragment.setUsersType(1);
        friendsUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(friendsUsersFragment, "Друзья");

        UsersFragment groupsUsersFragment = new UsersFragment();
        groupsUsersFragment.setUsersType(2);
        groupsUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(groupsUsersFragment, "Группа");

        UsersFragment teachersUsersFragment = new UsersFragment();
        teachersUsersFragment.setUsersType(3);
        teachersUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(teachersUsersFragment, "Преподаватели");
        UsersFragment blacklistUsersFragment = new UsersFragment();
        blacklistUsersFragment.setUsersType(4);
        blacklistUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(blacklistUsersFragment, "Заблокированные");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void runSearch(final String searchTerm){
        SearchTask searchTask = new SearchTask();
        searchTask.execute(searchTerm);
    }


    private class SearchTask extends AsyncTask<String, Void, ArrayList<User>>{

        ProgressDialog progressDialog = ProgressDialog.show(UsersActivity.this, "",
                "Идет поиск", true);

        public SearchTask(){
            progressDialog.setCancelable(true);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(UsersActivity.this, "Поиск отменен",
                            Toast.LENGTH_LONG).show();
                    cancel(true);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            ArrayList<User> usersList = new ArrayList<>();
            String url = "http://you.com.ru/user/user_search/action?mobile=1";
            HashMap<String, String> httpParams = new HashMap<>();
            httpParams.put("name", params[0]);
            String jsonData = Common.httpPost(url, Common.getLoginDataFromPref(UsersActivity.this), httpParams);

            JSONObject searchUsersJson = null;

            try {
                searchUsersJson = new JSONObject(jsonData);
                JSONArray usersArray = searchUsersJson.getJSONArray("users");
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userJson = usersArray.getJSONObject(i);
                    User user = new User();
                    user.setId(userJson.getInt("id"));
                    user.setPerson(userJson.getInt("person"));
                    user.setName(userJson.getString("name"));
                    user.setCode(userJson.getString("code"));
                    user.setPhoto(userJson.getInt("photo"));
                    usersList.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return usersList;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);

            progressDialog.dismiss();
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(UsersActivity.this);
            builderSingle.setTitle("Найденные пользователи");

            final ImageAdapter imageAdapter = new ImageAdapter(users, UsersActivity.this,0);

            builderSingle.setNegativeButton(
                    "Назад",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builderSingle.setAdapter(
                    imageAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = imageAdapter.getItem(which);
                            Intent intent = new Intent(UsersActivity.this, PersonActivity.class);
                            Bundle extras = new Bundle();
                            if (user.getPerson() == 0) {
                                extras.putString("person", String.valueOf(user.getId()));
                            } else {
                                extras.putString("person", String.valueOf(user.getPerson()));
                            }
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
            builderSingle.show();
        }
    }

}
