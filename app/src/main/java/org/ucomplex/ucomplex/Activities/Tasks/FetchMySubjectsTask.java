package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.StudyStructure.Department;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.StudyStructure.Progress;
import org.ucomplex.ucomplex.Model.Role;
import org.ucomplex.ucomplex.Model.StudyStructure.Course;
import org.ucomplex.ucomplex.Model.Users.Student;
import org.ucomplex.ucomplex.Model.Users.Teacher;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class FetchMySubjectsTask extends AsyncTask<Void, Void, Course> {
    //
    Activity mContext;
    String jsonData;
    int gcourse;

    public FetchMySubjectsTask(){

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


    private Course getCourseDataFromJson(String jsonData){
        ArrayList<Role> userRoles = new ArrayList<>();
        JSONObject courseJson = null;
        try {
            Course course = new Course();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String loggedUserStr = prefs.getString("loggedUser", "");
            Gson gson = new Gson();
            Student student = gson.fromJson(loggedUserStr, Student.class);

            courseJson = new JSONObject(jsonData);
            JSONObject courseArray = courseJson.getJSONObject("course");

            JSONObject teacherArray = courseJson.getJSONObject("teacher");
            JSONObject departmentArray = new JSONObject();
            Department department = new Department();
            if(courseJson.has("depart")){
                try{
                    boolean a = courseJson.getBoolean("depart");
                    course.setDepartment(new Department());
                }catch (JSONException e){
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
            JSONObject progressArray = courseJson.getJSONObject("progress");
            JSONArray  filesArray = courseJson.getJSONArray("files");
//used
            Teacher mainTeacher  = new Teacher();
            mainTeacher.setId(Integer.valueOf(teacherArray.getString("id")));
            mainTeacher.setName(teacherArray.getString("name"));
            mainTeacher.setPhoto(teacherArray.getInt("photo"));
            mainTeacher.setCode(teacherArray.getString("code"));
            mainTeacher.setDepartment(department);

            course.addTeacher(mainTeacher);

            ArrayList<File> files = new ArrayList<>();
            for(int i=0;i<filesArray.length();i++){

                JSONObject jsonFiles = filesArray.getJSONObject(i);
                JSONObject teacher = jsonFiles.getJSONObject("teacher");
                int teacherId = teacher.getInt("id");

                JSONArray filesArrayObject= jsonFiles.getJSONArray("files");
                for(int j=0;j<filesArrayObject.length();j++){
                    JSONObject jsonFile = filesArrayObject.getJSONObject(j);

                    File file = new File();
                    file.setId(jsonFile.getString("id"));
                    file.setName(jsonFile.getString("name"));
                    file.setAddress(jsonFile.getString("address"));
                    file.setData(jsonFile.getString("data"));

                    if(teacherId==mainTeacher.getId()){
                        file.setOwner(mainTeacher);
                    }else{
                        Teacher teacher1 = new Teacher();
                        teacher1.setId(teacherId);
                        teacher1.setName(teacher.getString("name"));
                        teacher1.setCode(teacher.getString("code"));
                        teacher1.setPhoto(teacher.getInt("photo"));
                        file.setOwner(teacher1);
                    }
                    file.setSize(jsonFile.getInt("size"));
                    file.setTime(jsonFile.getString("time"));
                    file.setType(jsonFile.getString("type"));
                files.add(file);
                }
            }
//used
            Progress progress = new Progress();
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
        return null;
    }

    @Override
    protected Course doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student/ajax/my_subjects?json";
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("subjId",this.getGcourseString());
        jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext), postParams);
        return getCourseDataFromJson(jsonData);
    }

    @Override
    protected void onPostExecute(final Course success) {

    }
}
