package org.ucomplex.ucomplex.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import org.javatuples.Pair;
import org.ucomplex.ucomplex.Adaptors.TeacherProfileAdapter;
import org.ucomplex.ucomplex.Model.TeacherInfo;
import org.ucomplex.ucomplex.Model.TeacherRating;

import java.util.ArrayList;

/**
 * Created by Sermilion on 04/05/16.
 */
public class TeacherProfileFragment extends ListFragment {

    private TeacherInfo mTeacherInfo;
    private Context mContext;

    public void setmTeacherRating(TeacherInfo teacherInfo) {
        this.mTeacherInfo = teacherInfo;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("Активность:", String.valueOf(mTeacherInfo.getActivity())));
        if(mTeacherInfo.getFacultyName().length()>0 && mTeacherInfo.getDepartmentName().length()>0){
            list.add(new Pair<>("Факультет:", mTeacherInfo.getFacultyName()));
            list.add(new Pair<>("Кафедра:", mTeacherInfo.getDepartmentName()));
        }else {
            list.add(new Pair<>("Профиль закрыт", ""));
        }
        TeacherProfileAdapter mAdapter = new TeacherProfileAdapter(mContext, list);
        setListAdapter(mAdapter);
        getListView().setDivider(null);
    }


}
