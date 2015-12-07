package org.ucomplex.ucomplex.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ucomplex.ucomplex.Activities.CourseActivity;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.R;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class CourseInfoFragment extends Fragment {

    private int type;
    private Course courseData;
    Activity mContext;
    Bitmap bitmap;


    ImageView teacherImageView;
    TextView teacherNameView;
    TextView courseNameView;
    TextView departmentNameView;
    TextView averageMarksView;
    TextView attendanceView;
    TextView departmentNameLabelView;

    public Activity getmContext() {
        return mContext;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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


        Bundle bundle = this.getArguments();
        if(bundle!=null){
            courseData = (Course) bundle.getSerializable("courseData");
            System.out.println();
        }
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_course_info, container, false);
        teacherImageView = (ImageView) rootView.findViewById(R.id.course_info_teacher_image);
        teacherNameView = (TextView) rootView.findViewById(R.id.course_info_teacher_name);
        courseNameView = (TextView) rootView.findViewById(R.id.course_info_course_name);
        departmentNameView = (TextView) rootView.findViewById(R.id.course_info_course_department_name);
        departmentNameLabelView = (TextView) rootView.findViewById(R.id.course_info_course_department_label);
        averageMarksView = (TextView) rootView.findViewById(R.id.course_info_course_average_mark_value);
        attendanceView = (TextView) rootView.findViewById(R.id.course_info_course_attendance_value);
//        teacherImageView.setImageBitmap(this.bitmap);
        if(this.bitmap==null){
            FetchPhotoTask fpt = new FetchPhotoTask();
            try {
                this.bitmap = fpt.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        teacherImageView.setImageBitmap(this.bitmap);





        if(courseData.getDepartment().getId()==-1){
            departmentNameView.setVisibility(View.GONE);
            departmentNameLabelView.setVisibility(View.GONE);
        }else{
            departmentNameView.setText(courseData.getDepartment().getName());
        }
        teacherNameView.setText(courseData.getTeacher(0).getName());
        courseNameView.setText(courseData.getName());
        int a = courseData.getProgress().getAbsence();
        int b = courseData.getProgress().getHours();
        double absence = ((double)a/(double)b)*100;
        DecimalFormat df = new DecimalFormat("#.##");
        absence = Double.valueOf(df.format(absence));
        averageMarksView.setText(String.valueOf(courseData.getProgress().getMark()));
        attendanceView.setText(String.valueOf(String.valueOf(absence)+"%"));
        return rootView;
    }

    private Double round2(Double val) {
        return new BigDecimal(val.toString()).setScale(2,RoundingMode.HALF_UP).doubleValue();
    }


    private class FetchPhotoTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            return getBitmapFromURL(courseData.getTeacher(0).getCode());
        }


        @Override
        protected void onPostExecute(final Bitmap success) {

        }

        public  Bitmap getBitmapFromURL(String src) {
            try {
                String urlStr = "https://ucomplex.org/files/photos/"+src+".jpg";
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }
    }

}
