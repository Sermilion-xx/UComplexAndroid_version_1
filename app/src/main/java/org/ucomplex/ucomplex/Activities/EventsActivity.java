package org.ucomplex.ucomplex.Activities;

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
import org.ucomplex.ucomplex.Adaptors.MenuAdapter;
import org.ucomplex.ucomplex.Fragments.EventsFragment;
import org.ucomplex.ucomplex.Model.EventRowItem;
import org.ucomplex.ucomplex.Model.Users.Student;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class EventsActivity extends AppCompatActivity {

    ArrayList eventsArray = null;
    FetchUserEventsTask mEventsTask = null;
    final String[] TITLES = { "События", "Календарь", "Сообщения", "Предметы", "Расписание","Пользователи", "Выход" };
    final int[] ICONS = { R.drawable.ic_menu_event,
            R.drawable.ic_menu_calendar,
            R.drawable.ic_menu_message,
            R.drawable.ic_menu_subject,
            R.drawable.ic_menu_timetable,
            R.drawable.ic_menu_users,
            R.drawable.ic_menu_exit};

    String NAME = "Авторханова Мадина";
    String EMAIL = "avtorkhanova@mail.ru";
    int PROFILE = R.mipmap.ic_no_image;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    private ArrayList<EventRowItem> parseJSON(String json){

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Student student = (Student)getIntent().getExtras().getSerializable("student");



        mEventsTask = new FetchUserEventsTask(this);
        try {
            this.eventsArray = mEventsTask.execute((Void) null).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);



        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new MenuAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer, toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };
        // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


//        globals.setShareInstance(eventsArray);
        EventsFragment fragment = new EventsFragment();
        Bundle data = new Bundle();
        data.putSerializable("eventItems", eventsArray);
        fragment.setArguments(data);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
