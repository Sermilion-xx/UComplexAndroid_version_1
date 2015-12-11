package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.javatuples.Triplet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.MyServices;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 10/12/2015.
 */
public class FetchSubjectsTask extends AsyncTask<Void, Void, ArrayList<Triplet<String, String, Integer>>> {

    Activity mContext;
    String[] assesmentType = {"Зачет","Экзамен", "Самостоятельная работа"};

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public FetchSubjectsTask(){

    }

    @Override
    protected ArrayList<Triplet<String, String, Integer>> doInBackground(Void... params) {
        String urlString = "http://you.com.ru/student/subjects_list?json";
        String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        return getSubjectDataFromJson(jsonData);
    }

    private ArrayList<Triplet<String, String, Integer>> getSubjectDataFromJson(String jsonData){
        ArrayList<Triplet<String, String, Integer>> subjectsListArray = new ArrayList<>();
        JSONObject subjectsJson = null;

        try {
            subjectsJson = new JSONObject(jsonData);
            JSONObject courses = subjectsJson.getJSONObject("courses");
            JSONObject coursesForms = subjectsJson.getJSONObject("courses_forms");
            JSONArray studentSubjectsList = subjectsJson.getJSONArray("studentSubjectsList");
            HashMap<String, String> hashCourses = (HashMap<String, String>) Common.parseJsonKV(courses);
            HashMap<String, String> hashCoursesForms = (HashMap<String, String>) Common.parseJsonKV(coursesForms);

            ArrayList<HashMap<String, String>> studentSubjectsListHashMap = new ArrayList<>();
            for(int i =0;i<studentSubjectsList.length();i++){
                HashMap<String, String> hashSubj = (HashMap<String, String>) Common.parseJsonKV(studentSubjectsList.getJSONObject(i));
                studentSubjectsListHashMap.add(hashSubj);
            }

            for(int i = 0; i<studentSubjectsListHashMap.size();i++){
                int    gcourse = Integer.parseInt(studentSubjectsListHashMap.get(i).get("id"));
                String _courseNameId = studentSubjectsListHashMap.get(i).get("course");
                String courseName = hashCourses.get(_courseNameId);
                int courseFrom = Integer.parseInt(hashCoursesForms.get(_courseNameId));
                Triplet<String, String, Integer> subject = new Triplet<>(courseName, assesmentType[courseFrom], gcourse);
                subjectsListArray.add(subject);
            }

            return subjectsListArray;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
