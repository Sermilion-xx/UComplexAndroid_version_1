package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ucomplex.ucomplex.Activities.MessagesActivity;
import org.ucomplex.ucomplex.Activities.Tasks.FetchPersonTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.Users.Teacher;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class CourseInfoFragment extends ListFragment implements OnTaskCompleteListener {

    private Course courseData;
    User user;
    Activity mContext;
    Bitmap bitmap;
    int person = -1;


    ImageView userImageView;
    TextView userNameView;
    TextView courseNameView;
    TextView departmentNameView;
    TextView averageMarksView;
    TextView attendanceView;
    TextView departmentNameLabelView;
    TextView atSignIconTextView;
    TextView mailTextView;
    TextView userIconTextView;
    TextView userPositionTextView;
    TextView userIconTextView2;
    TextView userPositionTextView2;
    ArrayList<View> views = new ArrayList<>();

    Button messageButton;
    Button friendButton;
    Button blacklistButton;

    public CourseInfoFragment() {
        // Required empty public constructor
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public Activity getmContext() {
        return mContext;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_course_info, container, false);
        messageButton = (Button) rootView.findViewById(R.id.course_info_button_message);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companion;
                String name;
                Intent intent = new Intent(getContext(), MessagesActivity.class);
                if(user.getPerson()==0){
                    companion = String.valueOf(user.getId());
                }else{
                    companion = String.valueOf(user.getPerson());
                }
                name = String.valueOf(user.getName());
                intent.putExtra("companion", companion);
                intent.putExtra("name", name);
                getContext().startActivity(intent);
            }
        });

        friendButton = (Button) rootView.findViewById(R.id.course_info_button_to_friend);
        blacklistButton = (Button) rootView.findViewById(R.id.course_info_button_block);
        userImageView = (ImageView) rootView.findViewById(R.id.course_info_teacher_image);
        if(this.bitmap!=null){
            userImageView.setImageBitmap(this.bitmap);
        }
        userNameView = (TextView) rootView.findViewById(R.id.course_info_teacher_name);
        mailTextView = (TextView) rootView.findViewById(R.id.course_info_mail);
        userPositionTextView = (TextView) rootView.findViewById(R.id.course_info_position);
        userPositionTextView2 = (TextView) rootView.findViewById(R.id.course_info_position_2);
        userPositionTextView2.setVisibility(View.GONE);

        userIconTextView = (TextView) rootView.findViewById(R.id.course_info_user_icon);
        atSignIconTextView = (TextView) rootView.findViewById(R.id.course_info_at_sign);

        atSignIconTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf"));
        atSignIconTextView.setText("\uF1FA");

        userIconTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf"));
        userIconTextView.setText("\uF007");

        userIconTextView2 = (TextView) rootView.findViewById(R.id.course_info_user_icon_2);
        userIconTextView2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf"));
        userIconTextView2.setText("\uF007");
        userIconTextView2.setVisibility(View.GONE);

        courseNameView = (TextView) rootView.findViewById(R.id.course_info_course_name);
        departmentNameView = (TextView) rootView.findViewById(R.id.course_info_course_department_name);
        departmentNameLabelView = (TextView) rootView.findViewById(R.id.course_info_course_department_label);
        averageMarksView = (TextView) rootView.findViewById(R.id.course_info_course_average_mark_value);
        attendanceView = (TextView) rootView.findViewById(R.id.course_info_course_attendance_value);

        views.add(userImageView);
        views.add(userNameView);
        views.add(courseNameView);
        views.add(departmentNameView);
        views.add(averageMarksView);
        views.add(attendanceView);
        views.add(departmentNameLabelView);
        views.add(atSignIconTextView);
        views.add(mailTextView);
        views.add(userIconTextView);
        views.add(userPositionTextView);
        views.add(userIconTextView2);
        views.add(userPositionTextView2);
        views.add(messageButton);
        views.add(friendButton);
        views.add(blacklistButton);

        for(View view:views){
            view.setVisibility(View.GONE);
        }

        Bundle bundle = this.getArguments();
        if(bundle!=null || courseData!=null){
            for(View view:views){
                view.setVisibility(View.VISIBLE);
            }
            if (bundle != null && bundle.containsKey("courseData")) {
                if(this.bitmap == null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            userImageView.setImageBitmap(bitmap);
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            bitmap = Common.getBitmapFromURL(courseData.getTeacher(0).getCode());
                            return null;
                        }
                    }.execute();
                }else {
                    userImageView.setImageBitmap(bitmap);
                }
                courseData = (Course) bundle.getSerializable("courseData");
                if (courseData != null) {
                    if (courseData.getDepartment().getId() == -1) {
                        departmentNameView.setVisibility(View.GONE);
                    } else {
                        departmentNameView.setText(courseData.getDepartment().getName());
                    }
                }
                courseNameView.setText(courseData.getName());
                int a = courseData.getProgress().getAbsence();
                int b = courseData.getProgress().getHours();
                double absence = ((double) a / (double) b) * 100;
                DecimalFormat df = new DecimalFormat("#.##");
                absence = Double.valueOf(df.format(absence));
                averageMarksView.setText(String.valueOf(courseData.getProgress().getMark()));
                attendanceView.setText(String.valueOf(String.valueOf(absence) + "%"));
            }
        }else{
            courseNameView.setVisibility(View.GONE);
            departmentNameView.setVisibility(View.GONE);
            averageMarksView.setVisibility(View.GONE);
            attendanceView.setVisibility(View.GONE);
            departmentNameLabelView.setVisibility(View.GONE);
            rootView.findViewById(R.id.course_info_course_average_mark_label).setVisibility(View.GONE);
            rootView.findViewById(R.id.course_info_course_attendance_label).setVisibility(View.GONE);
        }
                if (person == -1) {
                    if(courseData!=null){
                        person = courseData.getTeacher(0).getId();
                    }
                }
                if (user == null) {
                    if(mContext!=null){
                        //fetch data about person
                        FetchPersonTask fetchPersonTask = new FetchPersonTask(getActivity(), this);
                        fetchPersonTask.setPerson(String.valueOf(person));
                        fetchPersonTask.setmContext(mContext);
                        fetchPersonTask.setupTask();
                    }
                }else{
                    fillUserInfo();
                }
        return rootView;
    }

    private void fillUserInfo(){
        mailTextView.setText(user.getEmail());

        if(user.is_friend()){
            friendButton.setText("Удалить");
        }else{
            friendButton.setText("В друзья");
        }
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("user", String.valueOf(user.getId()));
                if(user.isReq_sent()) {
                    HandleMenuPress handleMenuPress = new HandleMenuPress();
                    handleMenuPress.execute("http://you.com.ru/user/friends/accept?mobile=1", params);
                    user.setReq_sent(false);
                    Toast.makeText(getActivity(), user.getName() + " теперь ваш друг :)", Toast.LENGTH_SHORT).show();
                    friendButton.setText("Удалить");
                }else {
                    HandleMenuPress handleMenuPress = new HandleMenuPress();
                    handleMenuPress.execute("http://you.com.ru/user/friends/delete?mobile=1", params);
                    Toast.makeText(getActivity(), "Пользователь удален из друзей :(", Toast.LENGTH_SHORT).show();
                    user.setReq_sent(false);
                    friendButton.setText("В друзья");
                    Common.userListChanged = 1;
                }
            }
        });

        if(user.isIs_black()){
            blacklistButton.setText("Разблокировать");
        }else{
            blacklistButton.setText("Зазблокировать");
        }
        blacklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                if(!user.isIs_black()) {
                    params.put("user", String.valueOf(user.getId()));
                    HandleMenuPress handleMenuPress = new HandleMenuPress();
                    handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
                    Toast.makeText(getActivity(), "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
                    user.setIs_black(true);
                    blacklistButton.setText("Разблокировать");
                }else{
                    params.put("user", String.valueOf(user.getId()));
                    HandleMenuPress handleMenuPress1 = new HandleMenuPress();
                    handleMenuPress1.execute("http://you.com.ru/user/blacklist/delete", params);
                    Toast.makeText(getActivity(), "Пользователь удален из черного списка :)", Toast.LENGTH_SHORT).show();
                    user.setIs_black(false);
                    blacklistButton.setText("Зазблокировать");
                }
                Common.userListChanged = 4;
            }
        });
        userNameView.setText(user.getName());
        if(this.bitmap==null){
            userImageView.setImageDrawable(Common.getDrawable(user));
        }

        int roleCount = user.getRoles().size();
        if(roleCount>0){
            TextView[] positionViews = new TextView[roleCount];

            positionViews[0] = userPositionTextView;
            if (roleCount > 1) {
                positionViews[1] = userPositionTextView2;
                userIconTextView2.setVisibility(View.VISIBLE);
                userPositionTextView2.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < roleCount; i++) {
                String positionName = user.getRoles().get(i).getPositionName();
                if (user.getRoles().get(i).getType() == 4) {
                    positionViews[i].setText("Студент - " + positionName);
                } else if (user.getRoles().get(i).getType() == 3) {
                    Teacher teach = (Teacher) user.getRoles().get(i);
                    String position = String.valueOf(positionName.charAt(0)).toUpperCase() + positionName.substring(1, positionName.length())
                            + " - " + teach.getSectionName();
                    if(positionViews[i]!=null){
                        positionViews[i].setText(position);
                    }
                }
            }
        }else{
            userPositionTextView.setText("Администратор");
        }

    }


    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        try {
            user = (User) task.get();

            if(user != null) {
                for(View view:views){
                    view.setVisibility(View.VISIBLE);
                }
                fillUserInfo();
            }else{
                Toast.makeText(getActivity(), "Ошибка при загрузке пользователя!", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    class HandleMenuPress extends AsyncTask <Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Common.httpPost((String) params[0], Common.getLoginDataFromPref(getContext()), (HashMap<String, String>) params[1]);
            return null;
        }
    }

}
