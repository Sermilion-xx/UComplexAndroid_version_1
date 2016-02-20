package org.ucomplex.ucomplex.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.ucomplex.ucomplex.Fragments.CourseInfoFragment;
import org.ucomplex.ucomplex.R;

public class PersonActivity extends AppCompatActivity {

    int personId;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Профиль");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            personId = Integer.parseInt(extra.getString("person"));
            bitmap = extra.getParcelable("bitmap");
//            FetchPersonTask fetchPersonTask = new FetchPersonTask(this, this);
//            fetchPersonTask.setPerson(String.valueOf(personId));
//            fetchPersonTask.setmContext(this);
//            fetchPersonTask.setupTask();
        }

        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
        courseInfoFragment.setPerson(personId);
        courseInfoFragment.setBitmap(bitmap);
        courseInfoFragment.setmContext(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_person, courseInfoFragment);
        fragmentTransaction.commit();

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


//    @Override
//    public void onTaskComplete(AsyncTask task, Object... o) {
//        try {
//            User mUser = (User) task.get();
//            if (mUser != null) {
//                List<Triplet> items = new ArrayList<>();
//                Triplet<Bitmap, User, String> item = new Triplet<>(bitmap, mUser, "-1");
//                items.add(item);
//                for (User role : mUser.getRoles()) {
//                    Triplet<String, String, String> aItem = null;
//                    String positionName = role.getPositionName();
//                    if(role.getType()==4){
//                        aItem = new Triplet<>(positionName, String.valueOf(role.getType()), "-1");
//                    }else{
//                        Teacher teach = (Teacher) role;
//                        aItem = new Triplet<>(teach.getSectionName(), role.getPositionName(),  "-1");
//                    }
//                    items.add(aItem);
//                }
//
//                ProfileFragment profileFragment = new ProfileFragment();
//                profileFragment.setContext(this);
//                profileFragment.setBitmap(bitmap);
//                profileFragment.setPerson(personId);
//                profileFragment.setmUser(mUser);
//                profileFragment.setmItems(items);
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction =
//                        fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.content_person, profileFragment);
//                fragmentTransaction.commit();
//            } else {
//                Toast.makeText(this, "Ошибка при загрузке пользователя!", Toast.LENGTH_SHORT).show();
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }

}
