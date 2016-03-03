package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import org.javatuples.Quartet;
import org.ucomplex.ucomplex.Activities.ProfileActivity;
import org.ucomplex.ucomplex.Adaptors.CourseInfoAdapter;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.Users.Teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Sermilion on 23/02/16.
 */
public class CourseFragment extends ListFragment {

    private Course courseData;
    private Activity mContext;
    CourseInfoAdapter mAdapter;
    //bitmap, name, code, id
    ArrayList<Quartet<String, String, String, String>> mItems = new ArrayList<>();

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (courseData == null) {
            courseData = (Course) bundle.getSerializable("courseData");
            if (courseData != null) {
                ArrayList<Teacher> teachers = courseData.getTeachers();
                Quartet<String, String, String, String> separatorItem1 = new Quartet<>("-1", "-1", "-1", "2");
                mItems.add(separatorItem1);
                for (Teacher teacher : teachers) {
                    String code = "";
                    if (teacher.getPhoto() == 1) {
                        code = teacher.getCode();
                    }
                    Quartet<String, String, String, String> item = new Quartet<>(code, teacher.getName(), String.valueOf(teacher.getId()), "1");
                    mItems.add(item);
                }
                Quartet<String, String, String, String> separatorItem = new Quartet<>("-1", "-1", "-1", "2");
                mItems.add(separatorItem);
                int a = courseData.getProgress().getAbsence();
                int b = courseData.getProgress().getHours();
                double absence = 100;
                if(a!=0 && b!=0){
                    absence = ((double) a / (double) b) * 100;
                    if (absence == 0.0) {
                        absence = 100;
                    }
                }
                DecimalFormat df = new DecimalFormat("#.##");
                absence = Double.valueOf(df.format(absence));
                Quartet<String, String, String, String> item = new Quartet<>(String.valueOf(absence) + "%", String.valueOf(courseData.getProgress().getMark()), "-1", "3");
                mItems.add(item);
                mAdapter = new CourseInfoAdapter(mItems, mContext);
                setListAdapter(mAdapter);
            }
        }

    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Intent intent = new Intent(getContext(), ProfileActivity.class);
//        Bundle extras = new Bundle();
//        extras.putString("person", String.valueOf(mItems.get(position).getValue2()));
//        intent.putExtras(extras);
//        startActivity(intent);
    }

}
