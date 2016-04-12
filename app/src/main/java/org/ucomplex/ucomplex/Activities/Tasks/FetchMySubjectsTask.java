package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Activities.CourseActivity;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.StudyStructure.Department;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.StudyStructure.Progress;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class FetchMySubjectsTask extends AsyncTask<Void, String, Course> implements IProgressTracker, DialogInterface.OnCancelListener {

    Activity mContext;
    String jsonData;
    int gcourse;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private String mProgressMessage;
    CourseActivity caller;
    Course course;

    public FetchMySubjectsTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.caller = (CourseActivity) mContext;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public String getGcourseString() {
        return String.valueOf(gcourse);
    }

    public void setGcourse(int gcourse) {
        this.gcourse = gcourse;
    }

    public void setupTask(Void... params) {
        this.setProgressTracker(this);
        this.execute(params);
    }

    private Course getCourseDataFromJson(String jsonData) {
        JSONObject courseJson = null;
        try {
            course = new Course();

            if(jsonData==null){
                jsonData = "";
            }
            courseJson = new JSONObject(jsonData);
            JSONObject courseArray = courseJson.getJSONObject("course");

            JSONObject departmentArray;
            Department department = new Department();
            if (courseJson.has("depart")) {
                try {
                    boolean a = courseJson.getBoolean("depart");
                    course.setDepartment(new Department());
                } catch (JSONException e) {
                    departmentArray = courseJson.getJSONObject("depart");
                    //used
                    department.setId(departmentArray.getInt("id"));
                    department.setName(departmentArray.getString("name"));
                    department.setPostcode(departmentArray.getString("postcode"));
                    department.setDescription(departmentArray.getString("description"));
                    department.setFax(departmentArray.getString("fax"));
                    department.setAddress(departmentArray.getString("address"));
                    department.setTel(departmentArray.getString("tel"));
                    department.setEmail(departmentArray.getString("email"));
                    department.setStruct_file(departmentArray.getString("struct_file"));
                    department.setAlias(departmentArray.getString("alias"));
                    department.setFaculty(departmentArray.getInt("faculty"));
                    department.setClient(departmentArray.getInt("client"));
                    course.setDepartment(department);
                }
            }
            JSONObject progressArray = new JSONObject();
            JSONArray filesArray = new JSONArray();
            if(!(courseJson.get("progress") instanceof Boolean)){
                progressArray = courseJson.getJSONObject("progress");
            }
            if(!(courseJson.get("files") instanceof Boolean)){
                filesArray = courseJson.getJSONArray("files");
            }
//used
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < filesArray.length(); i++) {
                try {
                    JSONObject jsonFiles = filesArray.getJSONObject(i);
                    JSONObject teacher = jsonFiles.getJSONObject("teacher");
                    JSONArray filesArrayObject = jsonFiles.getJSONArray("files");
                    User teacher1 = new User();

                    if (!teacher.isNull("id")) {
                            teacher1 = new User();
                            teacher1.setName(teacher.getString("name"));
                            teacher1.setCode(teacher.getString("code"));
                            teacher1.setPhoto(teacher.getInt("photo"));
                            teacher1.setId(teacher.getInt("id"));
                            teacher1.setType(3);
                            course.addTeacher(teacher1);

                        for (int j = 0; j < filesArrayObject.length(); j++) {
                            JSONObject jsonFile = filesArrayObject.getJSONObject(j);
                            File file = new File();
                            file.setId(jsonFile.getString("id"));
                            file.setName(jsonFile.getString("name"));
                            file.setAddress(jsonFile.getString("address"));
                            file.setData(jsonFile.getString("data"));
                            file.setSize(jsonFile.getInt("size"));
                            file.setTime(jsonFile.getString("time"));
                            file.setType(jsonFile.getString("type"));
                            file.setOwner(teacher1);
                            files.add(file);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//used
            Progress progress = new Progress();
            if(progressArray.length()>0) {
                progress.setType(progressArray.getInt("type"));
                progress.set_mark(progressArray.getInt("_mark"));
                progress.setMark(progressArray.getInt("mark"));
                progress.setAbsence(progressArray.getInt("absence"));
                progress.setCourse(null);
                progress.setHours(progressArray.getInt("hours"));
                progress.setIndivid(progressArray.getInt("individ"));
                progress.setMarkCount(progressArray.getInt("marksCount"));
                progress.setStudent(progressArray.getInt("student"));
                progress.setTable(progressArray.getInt("table"));
                progress.setTime(progressArray.getInt("time"));
            }

                course.setName(courseArray.getString("name"));
                course.setId(courseArray.getInt("id"));
                course.setCourse(courseArray.getInt("course"));
                course.setGroup(courseArray.getInt("group"));
                course.setTable(courseArray.getInt("table"));
                course.setClient(courseArray.getInt("client"));
                course.setCourse_id(courseArray.getInt("course_id"));
                course.setDescription(courseArray.getString("description"));

                course.setProgress(progress);
                course.setFiles(files);

            return course;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Course();
    }

    @Override
    protected Course doInBackground(Void... params) {
        String urlString = "https://ucomplex.org/student/ajax/my_subjects?json";
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("subjId", this.getGcourseString());
        jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), postParams);
        if(jsonData!=null){
            return getCourseDataFromJson(jsonData);
        }else{
            return new Course();
        }

    }

    @Override
    protected void onPostExecute(final Course course) {
        super.onPostExecute(course);
        mTaskCompleteListener.onTaskComplete(this);
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        mProgressTracker = null;
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mProgressMessage = values[0];
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
        }
    }

    @Override
    public void onProgress(String message) {
    }

    @Override
    protected void onCancelled() {
        mProgressTracker = null;
    }


    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Идет загрузка данных");
            if (course != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
        mTaskCompleteListener.onTaskComplete(this);
    }
}


