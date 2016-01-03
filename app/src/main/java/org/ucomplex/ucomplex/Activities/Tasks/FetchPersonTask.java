package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.Student;
import org.ucomplex.ucomplex.Model.Users.Teacher;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.MyServices;


/**
 * Created by Sermilion on 19/12/2015.
 */
public class FetchPersonTask extends AsyncTask<Void, String, User> implements IProgressTracker, DialogInterface.OnCancelListener {

    String person;
    Activity mContext;
    User user;

    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;
    private final ProgressDialog mProgressDialog;

    public FetchPersonTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;

        this.mTaskCompleteListener = taskCompleteListener;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
    }

    public void setupTask(Void ... params) {
        this.setProgressTracker(this);
        this.execute(params);
    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @Override
    protected User doInBackground(Void... params) {
        String urlString = "http://you.com.ru/user/person/"+this.person +"?json";
        String jsonData = Common.httpPost(urlString, MyServices.getLoginDataFromPref(mContext));
        if(jsonData.length()>2){
            user = getUserDataFromJson(jsonData);
            if(user.getCode()!=null){
                Bitmap photoBitmap = Common.getBitmapFromURL(user.getCode());
                user.setPhotoBitmap(photoBitmap);
            }
        }
        return user;
    }

    private User getUserDataFromJson(String jsonData){
        User user;
        JSONObject userJson = null;
        try{
            userJson = new JSONObject(jsonData);
            user = new User();
            user.setId(userJson.getInt("id"));
            user.setName(userJson.getString("name"));
            user.setEmail(userJson.getString("email"));
            user.setCode(userJson.getString("code"));

            JSONArray rolesArray = userJson.getJSONArray("roles");
            for(int i=0;i<rolesArray.length();i++){
                User role = null;
                JSONObject roleJson = rolesArray.getJSONObject(i);
                if(roleJson.getInt("type")==3){
                    Teacher teacherRole = new Teacher();
                    teacherRole.setId(roleJson.getInt("id"));
                    teacherRole.setRate(roleJson.getInt("rate"));
                    teacherRole.setPosition(roleJson.getInt("position"));
                    teacherRole.setPerson(roleJson.getInt("person"));
                    teacherRole.setType(roleJson.getInt("type"));
                    teacherRole.setPositionName(roleJson.getString("position_name"));
                    teacherRole.setSection(roleJson.getInt("section"));
                    teacherRole.setSectionName(roleJson.getString("section_name"));
                    teacherRole.setLead(roleJson.getInt("lead"));
                    //-------------------------------------------------------
                    teacherRole.setStatuses(userJson.getString("statuses"));
                    String academicAwards = userJson.getString("academic_awards");
                    teacherRole.setAcademicAwards(academicAwards);
                    teacherRole.setAcademicRank(userJson.getInt("academic_rank"));
                    teacherRole.setAcademicDegree(userJson.getInt("academic_degree"));
                    teacherRole.setUpqualification(userJson.getString("upqualification"));
                    teacherRole.setPhoneWork(userJson.getString("phone_work"));
                    role = teacherRole;
                }else if(roleJson.getInt("type")==4){
                    Student studentRole = new Student();
                    studentRole.setId(roleJson.getInt("id"));
                    studentRole.setType(roleJson.getInt("type"));
                    studentRole.setGroup(roleJson.getInt("group"));
                    studentRole.setPosition(roleJson.getInt("position"));
                    studentRole.setMajor(roleJson.getInt("major"));
                    studentRole.setStudy(roleJson.getInt("study"));
                    studentRole.setStudy(roleJson.getInt("year"));
                    studentRole.setPayment(roleJson.getInt("payment"));
                    studentRole.setContract_year(roleJson.getInt("contract_year"));
                    studentRole.setPositionName(roleJson.getString("position_name"));
                    role = studentRole;
                }
                if(role!=null){
                    user.addRole(role);
                }
            }
            if(userJson.has("black")){
                JSONObject blackJson = userJson.getJSONObject("black");
                user.setIs_black(blackJson.getBoolean("is_black"));
                user.setMe_black(blackJson.getBoolean("me_black"));
            }
            if(userJson.has("friends")){
                JSONObject friendJson = userJson.getJSONObject("friends");
                user.setFriendRequested(friendJson.getBoolean("is_friend"));
                user.setReq_friend(friendJson.getBoolean("req_sent"));
            }

            return user;

        } catch (JSONException e) {
        e.printStackTrace();
    }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        onComplete();
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        mProgressTracker = null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        // Update progress message
        mProgressMessage = values[0];
        // And send it to progress tracker
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
        }
    }

    public void setProgressTracker(IProgressTracker progressTracker) {
        mProgressTracker = progressTracker;
        if (mProgressTracker != null) {
            mProgressTracker.onProgress("Загружаем пользователя");
            if (user != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    @Override
    public void onProgress(String message) {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        // Show current message in progress dialog
        mProgressDialog.setMessage(message);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
        mProgressDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Cancel task
        this.cancel(true);
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(this);
    }

    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        mProgressTracker = null;
    }
}
