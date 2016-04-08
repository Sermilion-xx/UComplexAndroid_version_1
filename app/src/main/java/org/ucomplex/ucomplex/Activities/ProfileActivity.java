package org.ucomplex.ucomplex.Activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Activities.Tasks.FetchPersonTask;
import org.ucomplex.ucomplex.Fragments.ProfileFragment;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sermilion on 21/02/16.
 */
public class ProfileActivity extends AppCompatActivity implements OnTaskCompleteListener {

    int personId;
    Bitmap bitmap;
    ProfileFragment profileFragment;
    Menu menu;
    User mUser;
    int hasPhoto;
    String code;
    LinearLayout linlaHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Профиль");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Bundle extra = getIntent().getExtras();

        if (extra != null) {
            personId = Integer.parseInt(extra.getString("person"));
            bitmap = extra.getParcelable("bitmap");
            hasPhoto = Integer.parseInt(extra.getString("hasPhoto"));
            code = extra.getString("code");
            profileFragment = new ProfileFragment();
            FetchPersonTask fetchPersonTask = new FetchPersonTask(this, this);
            fetchPersonTask.setPerson(String.valueOf(personId));
            fetchPersonTask.setmContext(this);
            fetchPersonTask.setupTask();
        }else{
            linlaHeaderProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_block:
//                MenuItem menuItem = menu.findItem(R.id.action_block);
                profileFragment.getmAdapter().blockUser();
//                if(mUser.is_black()){
//                    menuItem.setTitle("Разблокировать");
//                }else{
//                    menuItem.setTitle("Заблокировать");
//                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        try {
            mUser = (User) task.get();
            List<Triplet> items = new ArrayList<>();
            Triplet<Bitmap, User, String> item = new Triplet<>(bitmap, mUser, "-1");
            items.add(item);
            if(mUser!=null){
                for (User role : mUser.getRoles()) {
                    Triplet<String, String, String> aItem;
                    String positionName = role.getPositionName();
                    if(role.getType()==4){
                        aItem = new Triplet<>(positionName, String.valueOf(role.getType()), String.valueOf(role.getId()));
                    }else{
                        aItem = new Triplet<>(role.getSectionName(), role.getPositionName()+"/"+String.valueOf(role.getId()),  String.valueOf(role.getId()));
                    }
                    items.add(aItem);
                }
            }
            profileFragment.setContext(this);
            profileFragment.setBitmap(bitmap);
            profileFragment.setHasPhoto(hasPhoto);
            profileFragment.setPerson(personId);
            profileFragment.setmUser(mUser);
            profileFragment.setmItems(items);
            profileFragment.setCode(code);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_person, profileFragment);
            fragmentTransaction.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
            linlaHeaderProgress.setVisibility(View.GONE);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
