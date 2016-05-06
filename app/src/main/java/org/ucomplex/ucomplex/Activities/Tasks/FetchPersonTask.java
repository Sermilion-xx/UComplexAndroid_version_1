package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.IProgressTracker;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Users.User;


/**
 * Created by Sermilion on 19/12/2015.
 */
public class FetchPersonTask extends AsyncTask<Void, String, User> implements IProgressTracker, DialogInterface.OnCancelListener {

    String person;
    Activity mContext;
    User user;

    private IProgressTracker mProgressTracker;
    private final OnTaskCompleteListener mTaskCompleteListener;

    public FetchPersonTask(Activity context, OnTaskCompleteListener taskCompleteListener) {
        this.mContext = context;
        this.mTaskCompleteListener = taskCompleteListener;
    }

    public void setupTask(Void... params) {
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
        String urlString = "https://ucomplex.org/user/person/" + this.person + "?json";
        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext));
        if (jsonData != null && jsonData.length() > 2) {
            user = getUserDataFromJson(jsonData);
        }
        return user;
    }

    private void teacherAdminStudentCommonSetter(User user, JSONObject roleJson) {
        try {
            user.setRole(roleJson.getInt("role"));
            user.setId(roleJson.getInt("id"));
            user.setType(roleJson.getInt("type"));
            try {
                user.setPosition(roleJson.getInt("position"));
            } catch (JSONException ignored) {
            }
            try {
                user.setPositionName(roleJson.getString("position_name"));
            } catch (JSONException ignored) {
            }
            try {
                user.setRate(roleJson.getInt("rate"));
            } catch (JSONException ignored) {
            }
            try {
                user.setPerson(roleJson.getInt("person"));
            } catch (JSONException ignored) {
            }
            try {
                user.setPerson(roleJson.getInt("section"));
            } catch (JSONException ignored) {
            }
            try {
                user.setSectionName(roleJson.getString("section_name"));
            } catch (JSONException ignored) {
            }
            try {
                user.setLead(roleJson.getInt("lead"));
            } catch (JSONException ignored) {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private User getUserDataFromJson(String jsonData) {
        User user;
        JSONObject userJson;
        try {
            userJson = new JSONObject(jsonData);
            user = new User();
            user.setId(userJson.getInt("id"));
            user.setName(userJson.getString("name"));
            user.setEmail(userJson.getString("email"));
            user.setCode(userJson.getString("code"));
            user.setPhoto(userJson.getInt("photo"));
            user.setCode(userJson.getString("code"));

            JSONArray rolesArray = userJson.getJSONArray("roles");
            for (int i = 0; i < rolesArray.length(); i++) {
                User role = null;
                JSONObject roleJson = rolesArray.getJSONObject(i);
                if(roleJson.getInt("role")==user.getId()){
                    user.setRole(roleJson.getInt("role"));
                    user.setType(roleJson.getInt("type"));
                    user.setPerson(roleJson.getInt("person"));
                }
                if (roleJson.getInt("type") == 3) {
                    User teacherRole = new User();
                    teacherAdminStudentCommonSetter(teacherRole, roleJson);
                    teacherRole.setStatuses(userJson.getString("statuses"));
                    String academicAwards = userJson.getString("academic_awards");
                    teacherRole.setAcademicAwards(academicAwards);
                    teacherRole.setAcademicRank(userJson.getInt("academic_rank"));
                    teacherRole.setAcademicDegree(userJson.getInt("academic_degree"));
                    teacherRole.setUpqualification(userJson.getString("upqualification"));
                    teacherRole.setPhoneWork(userJson.getString("phone_work"));
                    user.addRole(teacherRole);
                } else if (roleJson.getInt("type") == 4) {
                    User studentRole = new User();
                    teacherAdminStudentCommonSetter(studentRole, roleJson);
                    try {
                        studentRole.setGroup(roleJson.getInt("group"));
                    } catch (JSONException ignored) {
                    }
                    studentRole.setMajor(roleJson.getInt("major"));
                    studentRole.setStudy(roleJson.getInt("study"));
                    studentRole.setStudy(roleJson.getInt("year"));
                    studentRole.setPayment(roleJson.getInt("payment"));
                    studentRole.setContract_year(roleJson.getInt("contract_year"));
                    user.addRole(studentRole);
                } else if (roleJson.getInt("type") == 1) {
                    User employeeRole = new User();
                    teacherAdminStudentCommonSetter(employeeRole, roleJson);
                    user.addRole(employeeRole);
                } else if (roleJson.getInt("type") == 0) {
                    User adminRole = new User();
                    teacherAdminStudentCommonSetter(adminRole, roleJson);
                    user.addRole(adminRole);
                }else if (roleJson.getInt("type") == 9) {
                    User aspirantRole = new User();
                    teacherAdminStudentCommonSetter(aspirantRole, roleJson);
                    aspirantRole.setName(roleJson.getString("name"));
                    aspirantRole.setCode(roleJson.getString("code"));
                    aspirantRole.setPhoto(roleJson.getInt("photo"));
                    aspirantRole.setBirthday(roleJson.getString("birthday"));
                    aspirantRole.setRow(roleJson.getInt("row"));
                    aspirantRole.setBenefit(roleJson.getInt("benefit"));
                    aspirantRole.setOut(roleJson.getInt("out"));
                    aspirantRole.setOriginal(roleJson.getInt("original"));
                    aspirantRole.setSelector(roleJson.getInt("selector"));
                    user.addRole(aspirantRole);
                }
            }
            if (userJson.has("black")) {
                JSONObject blackJson = userJson.getJSONObject("black");
                user.setIs_black(blackJson.getBoolean("is_black"));
                user.setMe_black(blackJson.getBoolean("me_black"));
            }
            if (userJson.has("friends")) {
                JSONObject friendJson = userJson.getJSONObject("friends");
                user.setIs_friend(friendJson.getBoolean("is_friend"));
                user.setReq_sent(friendJson.getBoolean("req_sent"));
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
        String mProgressMessage = values[0];
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
//        if (!mProgressDialog.isShowing()) {
//            mProgressDialog.show();
//        }
//        // Show current message in progress dialog
//        mProgressDialog.setMessage(message);
    }

    @Override
    public void onComplete() {
        mTaskCompleteListener.onTaskComplete(this);
//        mProgressDialog.dismiss();
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
