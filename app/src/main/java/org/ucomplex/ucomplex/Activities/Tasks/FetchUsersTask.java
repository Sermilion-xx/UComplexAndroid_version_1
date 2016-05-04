package org.ucomplex.ucomplex.Activities.Tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Model.Users.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 12/12/2015.
 */
public class FetchUsersTask extends AsyncTask<Integer, Void, ArrayList<User>> {

    Activity mContext;


    private ArrayList<User> onlineUserList = new ArrayList<>();

    public FetchUsersTask(Activity context) {
        this.mContext = context;
    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<User> doInBackground(Integer... params) {
        String urlString = null;
        String urlString0 = "https://ucomplex.org/student/online?mobile=1";
        String urlString2 = "https://ucomplex.org/student/ajax/my_group?mobile=1";
        String urlString3 = "https://ucomplex.org/student/ajax/my_teachers?mobile=1";

        String urlString4 = "https://ucomplex.org/user/blacklist?mobile=1";
        String urlString1 = "https://ucomplex.org/user/friends?mobile=1";

        String urlString10 = "https://ucomplex.org/teacher/online?mobile=1";
        String urlString5 = "https://ucomplex.org/teacher/ajax/my_department?mobile=1";
        String urlString12 = "https://ucomplex.org/teacher/ajax/my_groups?mobile=1";

        HashMap<String, String> postData = new HashMap<>();
        if (params[0] == 0) {
            urlString = urlString0;
            int quanitity = 0;
            if (params.length > 1) {
                quanitity = params[1];
            }
            postData.put("start", String.valueOf(quanitity));
        } else if (params[0] == 1) {
            urlString = urlString1;
        } else if (params[0] == 2) {
            urlString = urlString2;
        } else if (params[0] == 3) {
            urlString = urlString3;
        } else if (params[0] == 4) {
            urlString = urlString4;
        } else if (params[0] == 5) {
            urlString = urlString5;
        } else if (params[0] == 10) {
            urlString = urlString10;
        } else if (params[0] == 12) {
            urlString = urlString12;
        }

        String jsonData = Common.httpPost(urlString, Common.getLoginDataFromPref(mContext), postData);

        if (jsonData != null) {
            onlineUserList = getUserDataFromJson(jsonData, params[0]);
            return onlineUserList;

        }
        return new ArrayList<>();
    }


    public ArrayList<User> getUserDataFromJson(String jsonData, int getTypeInt) {

        JSONObject onlineUsersJson = null;
        ArrayList<User> usersList = new ArrayList<>();
        try {
            onlineUsersJson = new JSONObject(jsonData);
            String getType = "";
            if (getTypeInt == 1) {
                getType = "friends";
            } else {
                getType = "users";
            }
            JSONArray usersArray = onlineUsersJson.getJSONArray(getType);
            for (int i = 0; i < usersArray.length(); i++) {
                try {
                    JSONObject userJson = usersArray.getJSONObject(i);
                    int type = -1;
                    User user = new User();
                    if (userJson.has("type")) {
                        type = userJson.getInt("type");
                        user.setType(userJson.getInt("type"));
                    }else{
                        user.setType(-1);
                    }
                    if (type == 1) {
                        User admin = new User();
                        admin.setType(type);
                        user = admin;
                    } else if (type == 4) {
                        User student = new User();
                        student.setAlias(userJson.getString("alias"));
                        student.setType(type);
                        user = student;
                    }
                    if (getTypeInt == 3) {
                        User teacher = new User();
                        teacher.setSex(userJson.getInt("sex"));
                        teacher.setStatuses(userJson.getString("statuses"));
                        String academicAwards = userJson.getString("academic_awards");
                        teacher.setAcademicAwards(academicAwards);
                        teacher.setBirthday(userJson.getString("birthday"));
                        teacher.setBirthplace(userJson.getString("birthplace"));
                        teacher.setCountry(userJson.getInt("country"));
                        teacher.setAddressRegistration(userJson.getString("address_registration"));
                        teacher.setAddressLive(userJson.getString("address_live"));
                        teacher.setPhoneWork(userJson.getString("phone_work"));
                        teacher.setSeries(userJson.getString("series"));
                        teacher.setNumber(userJson.getString("number"));
                        teacher.setDocumentDate(userJson.getString("document_date"));
                        teacher.setDocumentDepart(userJson.getString("document_depart"));
                        teacher.setDocumentDepartCode(userJson.getString("document_depart_code"));
                        teacher.setAcademicDegree(userJson.getInt("academic_degree"));
                        teacher.setAcademicRank(userJson.getInt("academic_rank"));
                        teacher.setUpqualification(userJson.getString("upqualification"));
                        teacher.setType(getTypeInt);
                        user = teacher;
                    }

                    try {
                        if (getTypeInt != 3) {
                            user.setOnline(userJson.getInt("online"));
                            user.setAgent(userJson.getInt("agent"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    user.setId(userJson.getInt("id"));
                    try {
                        user.setPerson(userJson.getInt("person"));
                    } catch (JSONException ignored) {
                    }

                    user.setName(userJson.getString("name"));
                    user.setCode(userJson.getString("code"));
                    user.setPhoto(userJson.getInt("photo"));
                    if (userJson.has("friend")) {
                        user.setFriendRequested(true);
                    }
                    usersList.add(user);
                } catch (JSONException ignored) {
                }
            }
            return usersList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
