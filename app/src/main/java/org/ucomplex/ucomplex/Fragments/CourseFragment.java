package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Adaptors.CourseInfoAdapter;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.Users.Teacher;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Sermilion on 23/02/16.
 */
public class CourseFragment extends ListFragment {

    private Course courseData;
    private Activity mContext;
    CourseInfoAdapter mAdapter;
    ArrayList<Triplet<String, String, String>> mItems = new ArrayList<>();

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
        courseData = (Course) bundle.getSerializable("courseData");
        if(courseData!=null){
            ArrayList<Teacher> teachers = courseData.getTeachers();
            for(Teacher teacher:teachers){
                String code = "";
                if(teacher.getPhoto()==1){
                    code = teacher.getCode();
                }
                Triplet<String, String, String> item = new Triplet<>(code, teacher.getName(), String.valueOf(teacher.getPerson()));
                mItems.add(item);
            }
            int a = courseData.getProgress().getAbsence();
            int b = courseData.getProgress().getHours();
            double absence = ((double) a / (double) b) * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            absence = Double.valueOf(df.format(absence));
            Triplet<String, String, String> item = new Triplet<>(String.valueOf(absence) + "%", String.valueOf(courseData.getProgress().getMark()), "-1");
            mItems.add(item);
            mAdapter = new CourseInfoAdapter(mItems, mContext);
            setListAdapter(mAdapter);
        }

    }

}
