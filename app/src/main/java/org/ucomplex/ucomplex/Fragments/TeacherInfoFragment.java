package org.ucomplex.ucomplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.TeacherInfo;
import org.ucomplex.ucomplex.R;

/**
 * Created by Sermilion on 30/04/16.
 */
public class TeacherInfoFragment extends Fragment {

    private TeacherInfo teacherInfo = new TeacherInfo();

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_info, container, false);
        TextView disciplinesTextView = (TextView) view.findViewById(R.id.list_teacher_disciplines_value);
        TextView disiplinesInTimetable = (TextView) view.findViewById(R.id.list_teacher_disciplines_in_timetabl_values);
        TextView studyDegreeTextView = (TextView) view.findViewById(R.id.study_degree_value);
        TextView studyRankTextView = (TextView) view.findViewById(R.id.study_rank_value);
        TextView upqualificationsTextView = (TextView) view.findViewById(R.id.study_upqualification_value);
        TextView bioTextView = (TextView) view.findViewById(R.id.bio_value);
        if(teacherInfo.getId()!=0){
            String disciplines = teacherInfo.getCourses()!=null?teacherInfo.getCourses():"не указанно";
            disciplinesTextView.setText(Html.fromHtml(Html.fromHtml(disciplines).toString()));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < teacherInfo.getTeacherTimetableCourses().size(); i++) {
                stringBuilder.append(teacherInfo.getTeacherTimetableCourses().get(i).getName());
                if (i < teacherInfo.getTeacherTimetableCourses().size()) {
                    stringBuilder.append(", ");
                }
            }
            String disiplinesInTimetableStr = Html.fromHtml(Html.fromHtml(stringBuilder.toString()).toString()).toString();
            disiplinesInTimetable.setText(!disiplinesInTimetableStr.equals("")?disiplinesInTimetableStr:"не указанно");
            studyDegreeTextView.setText(Common.getDegree(teacherInfo.getDegree()));
            studyRankTextView.setText(Common.getRank(teacherInfo.getRank()));
            String upqualifications = teacherInfo.getUpqualification()!=null?teacherInfo.getUpqualification():"не указанно";
            upqualificationsTextView.setText(Html.fromHtml(upqualifications).toString());
            bioTextView.setText(Html.fromHtml(Html.fromHtml(teacherInfo.getBio()).toString()));
        }

        return view;
    }
}
