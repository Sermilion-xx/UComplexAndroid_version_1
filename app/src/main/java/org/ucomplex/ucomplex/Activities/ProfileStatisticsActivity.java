package org.ucomplex.ucomplex.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.javatuples.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.ProfileStatisticsFragment;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

public class ProfileStatisticsActivity extends AppCompatActivity {

    ArrayList<Pair<String, String>> mItems;
    ProfileStatisticsFragment profileStatisticsFragment;
    int role;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final User user = Common.getUserDataFromPref(this);
        toolbar.setTitle(user.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        role = Integer.parseInt(getIntent().getExtras().getString("role"));
        type = Integer.parseInt(getIntent().getExtras().getString("type"));
        profileStatisticsFragment = new ProfileStatisticsFragment();
        if(mItems==null){
            getStatistics(user);
        }
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

    private void getStatistics(final User user){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                String url = "http://you.com.ru/user/page/"+user.getId()+"?mobile=1";
                return Common.httpPost(url, Common.getLoginDataFromPref(ProfileStatisticsActivity.this));
            }

            @Override
            protected void onPostExecute(String jsonData) {
                super.onPostExecute(jsonData);
                JSONObject statisticsJson;
                mItems = new ArrayList<>();
                try {
                    if (jsonData != null) {
                        statisticsJson = new JSONObject(jsonData);
                        JSONObject progressJson = statisticsJson.getJSONObject("progress");
                        JSONObject rating = statisticsJson.getJSONObject("rating");

                        mItems.add(new Pair<>(Common.getStringUserType(ProfileStatisticsActivity.this, type), " "));
                        mItems.add(new Pair<>("Год поступления :", statisticsJson.getString("year")));
                        mItems.add(new Pair<>("Форма обучения :", Common.getStudyForm(ProfileStatisticsActivity.this,statisticsJson.getInt("study"))));
                        mItems.add(new Pair<>("Форма оплаты :", Common.getPayment(ProfileStatisticsActivity.this,statisticsJson.getInt("study"))));
                        mItems.add(new Pair<>("Уровень образования :", Common.getStudyLevel(ProfileStatisticsActivity.this,statisticsJson.getInt("study"))));
                        mItems.add(new Pair<>("Факультет :", statisticsJson.getString("faculty_name")));
                        mItems.add(new Pair<>("Специальность :", statisticsJson.getString("major_name")));
                        mItems.add(new Pair<>("Группа :", statisticsJson.getString("group_name")));

                        double marks = Common.round((double)progressJson.getInt("mark") / (double)progressJson.getInt("marksCount"),2);
                        mItems.add(new Pair<>("Средняя успеваемость :", String.valueOf(marks)));
                        double attendance =  Common.round(100-((double) progressJson.getInt("absence") / (double) progressJson.getInt("hours"))*100,2);
                        mItems.add(new Pair<>("Общая посещаемость :", String.valueOf(attendance)+" %"));

                        mItems.add(new Pair<>("Место в общем рейтинге :", rating.getString("general")));
                        mItems.add(new Pair<>("Место в рейтинге по факультету :", rating.getString("faculty")));

                        profileStatisticsFragment.setStatisticItems(mItems);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_profile_statistics, profileStatisticsFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }catch (JSONException ignored){}
            }
        }.execute();
    }
}
