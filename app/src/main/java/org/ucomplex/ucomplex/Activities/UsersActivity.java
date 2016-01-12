package org.ucomplex.ucomplex.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Adaptors.ImageAdapter;
import org.ucomplex.ucomplex.Adaptors.ViewPagerAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.UsersFragment;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MySearchBox;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.HashMap;


public class UsersActivity extends AppCompatActivity {

    MySearchBox search;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.users_toolbar);
        toolbar.setTitle("Онлайн");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        search = (MySearchBox) findViewById(R.id.searchbox);
        search.setLogoText("Поиск");
        search.setMenuListener(new SearchBox.MenuListener(){

            @Override
            public void onMenuClick() {
                search.showContextMenu();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {
                System.out.println();
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                System.out.println();
            }

            @Override
            public void onSearchTermChanged(String s) {
                System.out.println();
            }


            @Override
            public void onSearch(final String searchTerm) {
                SearchResult option = new SearchResult(searchTerm,getResources().getDrawable(R.drawable.ic_menu_users));
                search.addSearchable(option);
                runSearch(searchTerm);
            }

            @Override
            public void onResultClick(SearchResult result){
                //React to a result being clicked
                System.out.println();
            }


            @Override
            public void onSearchCleared() {
                System.out.println();
            }

        });






//        search.enableVoiceRecognition(UsersActivity.this);


        viewPager = (ViewPager) findViewById(R.id.users_viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    toolbar.setTitle("Онлайн");
                }else if(position==1){
                    toolbar.setTitle("Друзья");
                }else if(position==2){
                    toolbar.setTitle("Группа");
                }else if(position==3){
                    toolbar.setTitle("Преподаватели");
                }else if(position==4){
                    toolbar.setTitle("Заблокированные");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.users_tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);

        TextView tv0 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
        tv0.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        tv0.setTextSize(16);
        TextView tv1 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
        tv1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        tv1.setTextSize(18);
        TextView tv2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(2)).getChildAt(1));
        tv2.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        TextView tv3 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(3)).getChildAt(1));
        tv3.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
        TextView tv4 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(4)).getChildAt(1));
        tv4.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf"));
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        UsersFragment onlineUsersFragment = new UsersFragment();
        onlineUsersFragment.setUsersType(0);
        onlineUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(onlineUsersFragment, "\uF007");

        UsersFragment friendsUsersFragment = new UsersFragment();
        friendsUsersFragment.setUsersType(1);
        friendsUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(friendsUsersFragment, "\uF234");

        UsersFragment groupsUsersFragment = new UsersFragment();
        groupsUsersFragment.setUsersType(2);
        groupsUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(groupsUsersFragment, "\uF0C0");

        UsersFragment teachersUsersFragment = new UsersFragment();
        teachersUsersFragment.setUsersType(3);
        teachersUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(teachersUsersFragment, "\uF21B");

        UsersFragment blacklistUsersFragment = new UsersFragment();
        blacklistUsersFragment.setUsersType(4);
        blacklistUsersFragment.setActivity(UsersActivity.this);
        adapter.addFragment(blacklistUsersFragment, "\uF235");

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
