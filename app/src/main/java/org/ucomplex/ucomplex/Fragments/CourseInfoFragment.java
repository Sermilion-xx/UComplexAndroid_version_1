package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.Tasks.FetchPersonTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.Users.Teacher;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MyServices;
import org.ucomplex.ucomplex.R;

import java.text.DecimalFormat;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class CourseInfoFragment extends Fragment {

    private Course courseData;
    User user;
    Activity mContext;
    Bitmap bitmap;
    //4 - student, 3 - teacher
    int usetType;
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

    Button messageButton;
    Button friendButton;
    Button blacklistButton;


    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getUsetType() {
        return usetType;
    }

    public void setUsetType(int usetType) {
        this.usetType = usetType;
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

    public CourseInfoFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_course_info, container, false);
        messageButton = (Button) rootView.findViewById(R.id.course_info_button_message);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        friendButton = (Button) rootView.findViewById(R.id.course_info_button_to_friend);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("user", String.valueOf(user.getId()));
                if(user.isFriendRequested()) {
                    HandleMenuPress handleMenuPress = new HandleMenuPress();
                    handleMenuPress.execute("http://you.com.ru/user/friends/accept", params);
                    user.setFriendRequested(false);
                    Toast.makeText(getActivity(), user.getName() + " теперь ваш друг :)", Toast.LENGTH_SHORT).show();
                }else {
                    HandleMenuPress handleMenuPress = new HandleMenuPress();
                    handleMenuPress.execute("http://you.com.ru/user/friends/delete", params);
                    Toast.makeText(getActivity(), "Пользователь удален из друзей :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        blacklistButton = (Button) rootView.findViewById(R.id.course_info_button_block);
        blacklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("user", String.valueOf(user.getId()));
                HandleMenuPress handleMenuPress = new HandleMenuPress();
                handleMenuPress.execute("http://you.com.ru/user/blacklist/add", params);
                Toast.makeText(getActivity(), "Пользователь добавлен в черный список :(", Toast.LENGTH_SHORT).show();
            }
        });

        userImageView = (ImageView) rootView.findViewById(R.id.course_info_teacher_image);
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

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            if(bundle.containsKey("courseData")){
                courseData = (Course) bundle.getSerializable("courseData");

                int post = courseData.getTeacher(0).getPost();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String langStr = prefs.getString("lang", "");
                String postStr = "";
                try {
                    JSONObject langJsonObj = new JSONObject(langStr);
                    JSONObject langJson = langJsonObj.getJSONObject("lang");
                    JSONObject postJson = langJson.getJSONObject("post");
                    postStr = postJson.getString(String.valueOf(post));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(courseData.getDepartment().getId()==-1){
                    departmentNameView.setVisibility(View.GONE);
                }else{
                    departmentNameView.setText(courseData.getDepartment().getName());
                }

                courseNameView.setText(courseData.getName());
                int a = courseData.getProgress().getAbsence();
                int b = courseData.getProgress().getHours();
                double absence = ((double)a/(double)b)*100;
                DecimalFormat df = new DecimalFormat("#.##");
                absence = Double.valueOf(df.format(absence));
                averageMarksView.setText(String.valueOf(courseData.getProgress().getMark()));
                attendanceView.setText(String.valueOf(String.valueOf(absence)+"%"));
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
                    person = courseData.getTeacher(0).getPerson();
                }

                if (user == null) {
                    //fetch data about person
                    FetchPersonTask fetchPersonTask = new FetchPersonTask();
                    fetchPersonTask.setPerson(String.valueOf(person));
                    fetchPersonTask.setmContext(mContext);
                    try {
                        user = fetchPersonTask.execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

        if(user != null) {
            mailTextView.setText(user.getEmail());
            userNameView.setText(user.getName());
            if (user.getPhotoBitmap() != null) {
                bitmap = user.getPhotoBitmap();
                userImageView.setImageBitmap(this.bitmap);
            } else {
                userImageView.setImageDrawable(Common.getDrawable(user));
            }

            int roleCount = user.getRoles().size();
            TextView[] positionViews = new TextView[roleCount];
            ;
            positionViews[0] = userPositionTextView;
            if (roleCount > 1) {
                positionViews[1] = userPositionTextView2;
                userIconTextView2.setVisibility(View.VISIBLE);
                userPositionTextView2.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < roleCount; i++) {
                String positionName = user.getRoles().get(i).getPositionName();
                if (user.getRoles().get(i).getType() == 4) {
                    if (positionViews != null) {
                        positionViews[i].setText("Студент - " + positionName);
                    }
                } else if (user.getRoles().get(i).getType() == 3) {
                    Teacher teach = (Teacher) user.getRoles().get(i);
                    if (positionViews != null) {
                        String position = String.valueOf(positionName.charAt(0)).toUpperCase() + positionName.substring(1, positionName.length())
                                + " - " + teach.getSectionName();
                        positionViews[i].setText(position);
                    }
                }
            }

        }else{
            Toast.makeText(getActivity(), "Ошибка при загрузни пользователя!", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    class HandleMenuPress extends AsyncTask <Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Common.httpPost((String) params[0], MyServices.getLoginDataFromPref(getContext()), (HashMap<String, String>) params[1]);
            return null;
        }
    }

}
