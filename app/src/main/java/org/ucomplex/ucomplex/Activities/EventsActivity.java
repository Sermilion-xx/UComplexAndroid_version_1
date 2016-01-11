package org.ucomplex.ucomplex.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.ucomplex.ucomplex.Activities.Tasks.FetchUserEventsTask;
import org.ucomplex.ucomplex.Activities.Tasks.FetchUserLoginTask;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Adaptors.MenuAdapter;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Fragments.EventsFragment;
import org.ucomplex.ucomplex.Model.EventRowItem;
import org.ucomplex.ucomplex.Model.Users.Student;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity implements OnTaskCompleteListener, FetchUserLoginTask.AsyncResponse{

    ArrayList eventsArray = null;
    FetchUserEventsTask mEventsTask = null;
    User user;
    ProgressDialog dialog;

    final String[] TITLES = { "События", "Анкетирование", "Дисциплины", "Материалы", "Справки","Пользователи","Сообщения","Библиотека","Календарь","Настройки", "Выход" };
    final int[] ICONS = { R.drawable.ic_menu_event,
            R.drawable.ic_menu_questionare,
            R.drawable.ic_menu_materials,
            R.drawable.ic_menu_subject,
            R.drawable.ic_menu_materials,
            R.drawable.ic_menu_users,
            R.drawable.ic_menu_users,
            R.drawable.ic_menu_materials,
            R.drawable.ic_menu_calendar,
            R.drawable.ic_menu_materials,
            R.drawable.ic_menu_exit};

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    MenuAdapter mAdapter;                        // Declaring Adapter For Recycler View
    LinearLayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle


    private void refresh(){
        dialog = ProgressDialog.show(this, "",
                "Обновляется", true);
        dialog.show();
        user = Common.getUserDataFromPref(this);
        FetchUserLoginTask fetchUserLoginTask = new FetchUserLoginTask(user.getLogin(), user.getPass(), EventsActivity.this);
        fetchUserLoginTask.delegate = this;
        fetchUserLoginTask.execute();
        mEventsTask = new FetchUserEventsTask(this){
            @Override
            protected void onPostExecute(ArrayList<EventRowItem> items) {
                super.onPostExecute(items);
                eventsArray = items;
                dialog.dismiss();
            }
        };
        mEventsTask.execute();

        new FetchUserEventsTask(this){
            @Override
            protected void onPostExecute(ArrayList<EventRowItem> items) {
                super.onPostExecute(items);
                eventsArray = items;
                EventsFragment fragment = new EventsFragment();
                fragment.setContext(EventsActivity.this);
                Bundle data = new Bundle();
                data.putSerializable("eventItems", eventsArray);
                fragment.setArguments(data);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                dialog.dismiss();
            }

        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("События");
        setSupportActionBar(toolbar);
        user = Common.getUserDataFromPref(this);

        Bitmap bmp;
        if(Common.hasKeyPref(this, "profilePhoto")){
            bmp = Common.decodePhotoPref(this,"profilePhoto");
            user.setPhotoBitmap(bmp);
        }

        mEventsTask = (FetchUserEventsTask) new FetchUserEventsTask(this){
            @Override
            protected void onPostExecute(ArrayList<EventRowItem> items) {
                super.onPostExecute(items);
                eventsArray = items;
                EventsFragment fragment = new EventsFragment();
                fragment.setContext(EventsActivity.this);
                Bundle data = new Bundle();
                data.putSerializable("eventItems", eventsArray);
                fragment.setArguments(data);
                getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                dialog.dismiss();
            }

        }.execute();
        dialog = ProgressDialog.show(this, "",
                "Загружаются события", true);
        dialog.show();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MenuAdapter(TITLES,ICONS, user, this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer, toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }
        return id == android.R.id.home || super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {

    }

    @Override
    public void processFinish(Student output, Bitmap  bitmap) {
        if(output!=null) {
            Common.setUserDataToPref(this, output);
            mAdapter.setProfileBitmap(bitmap);
            if(bitmap!=null){
                Common.encodePhotoPref(this,bitmap, "profileBitmap");
                user.setPhotoBitmap(bitmap);
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void canceled(boolean canceled) {

    }
}
