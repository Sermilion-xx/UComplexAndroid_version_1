package org.ucomplex.ucomplex.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import org.javatuples.Triplet;
import org.ucomplex.ucomplex.Model.TeacherInfo;

import java.util.ArrayList;

/**
 * Created by Sermilion on 30/04/16.
 */
public class TeacherInfoAdapter extends ArrayAdapter<TeacherInfo> {

    LayoutInflater inflater;

    public TeacherInfoAdapter(Context context, int resource) {
        super(context, -1, new ArrayList<TeacherInfo>());
    }
}
