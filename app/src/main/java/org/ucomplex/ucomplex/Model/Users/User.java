package org.ucomplex.ucomplex.Model.Users;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.ucomplex.ucomplex.Model.StudyStructure.Department;
import org.ucomplex.ucomplex.Model.StudyStructure.File;
import org.ucomplex.ucomplex.Model.StudyStructure.Progress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class User implements Serializable {
    private int id;
    private int studyLevel;
    private String majorName;
    private String groupName;
    private int role;
    private int client;
    private String name;
    private String birthday;
    private String login;
    private String pass;
    private String code;
    private int photo;
    private String phone;
    private String email;
    private ArrayList<User> roles = new ArrayList<>();
    private int person;
    private int agent;
    private int online;
    private Bitmap photoBitmap;
    private String birthplace;
    private int country;
    private String addressRegistration;
    private String addressLive;
    private String phoneWork;
    private int type;
    private boolean friendRequested;
    private int position;
    private String positionName;
    private String session;
    private boolean is_black;
    private boolean me_black;
    private boolean req_sent;
    private boolean is_friend;
    private int ratingGeneral;
    private int ratingFaculty;
    private int row;
    private int benefit;
    private int out;
    private int original;
    private int selector;




    //Teacher
    private int post;
    private int experience;
    private int dep_experience;
    private ArrayList<String> courses;
    private int rank;
    private int degree;
    private String bio;
    private int plan;
    private int fact;
    private int fails;
    private String activity_update;
    private int selection;
    private Department department;
    private int departmentId;
    private String departmentName;
    private int closed;
    private ArrayList<File> files;
    private int sex;
    private String series;
    private String number;
    private String documentDate;
    private String documentDepart;
    private String documentDepartCode;
    private int academicDegree;
    private int academicRank;
    private String statuses;
    private String academicAwards;
    private String upqualification;
    private int rate;
    private int section;
    private String sectionName;
    private int lead;
    private int activity;
    private String facultyName;
    private ArrayList<TimetableEntry> timetableEntries;

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getSelector() {
        return selector;
    }

    public void setSelector(int selector) {
        this.selector = selector;
    }

    public int getBenefit() {
        return benefit;
    }

    public void setBenefit(int benefit) {
        this.benefit = benefit;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRatingGeneral() {
        return ratingGeneral;
    }

    public void setRatingGeneral(int ratingGeneral) {
        this.ratingGeneral = ratingGeneral;
    }

    public int getRatingFaculty() {
        return ratingFaculty;
    }

    public void setRatingFaculty(int ratingFaculty) {
        this.ratingFaculty = ratingFaculty;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public int getStudyLevel() {
        return studyLevel;
    }

    public void setStudyLevel(int studyLevel) {
        this.studyLevel = studyLevel;
    }

    public ArrayList<TimetableEntry> getTimetableEntries() {
        return timetableEntries;
    }

    public void setTimetableEntries(ArrayList<TimetableEntry> timetableEntries) {
        this.timetableEntries = timetableEntries;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getLead() {
        return lead;
    }

    public void setLead(int lead) {
        this.lead = lead;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getDep_experience() {
        return dep_experience;
    }

    public void setDep_experience(int dep_experience) {
        this.dep_experience = dep_experience;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getFact() {
        return fact;
    }

    public void setFact(int fact) {
        this.fact = fact;
    }

    public int getFails() {
        return fails;
    }

    public void setFails(int fails) {
        this.fails = fails;
    }

    public String getActivity_update() {
        return activity_update;
    }

    public void setActivity_update(String activity_update) {
        this.activity_update = activity_update;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentDepart() {
        return documentDepart;
    }

    public void setDocumentDepart(String documentDepart) {
        this.documentDepart = documentDepart;
    }

    public String getDocumentDepartCode() {
        return documentDepartCode;
    }

    public void setDocumentDepartCode(String documentDepartCode) {
        this.documentDepartCode = documentDepartCode;
    }

    public int getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(int academicDegree) {
        this.academicDegree = academicDegree;
    }

    public int getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(int academicRank) {
        this.academicRank = academicRank;
    }

    public String getStatuses() {
        return statuses;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public String getAcademicAwards() {
        return academicAwards;
    }

    public void setAcademicAwards(String academicAwards) {
        this.academicAwards = academicAwards;
    }

    public String getUpqualification() {
        return upqualification;
    }

    public void setUpqualification(String upqualification) {
        this.upqualification = upqualification;
    }

    //---


    private Progress progress;
    private String alias;
    private int group;
    private int major;
    private int study;
    private int year;
    private int payment;
    private int contract_year;

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getStudy() {
        return study;
    }

    public void setStudy(int study) {
        this.study = study;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getContract_year() {
        return contract_year;
    }

    public void setContract_year(int contract_year) {
        this.contract_year = contract_year;
    }

    //-----------

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean is_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public void addRole(User role){
        roles.add(role);
    }

    public boolean is_black() {
        return is_black;
    }

    public boolean isMe_black() {
        return me_black;
    }

    public void setMe_black(boolean me_black) {
        this.me_black = me_black;
    }

    public boolean isReq_sent() {
        return req_sent;
    }

    public void setReq_sent(boolean req_sent) {
        this.req_sent = req_sent;
    }

    public boolean isIs_black() {
        return is_black;
    }

    public void setIs_black(boolean is_black) {
        this.is_black = is_black;
    }



    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFriendRequested() {
        return friendRequested;
    }

    public void setFriendRequested(boolean friendRequested) {
        this.friendRequested = friendRequested;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public User(){
        this.id = -1;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getAddressLive() {
        return addressLive;
    }

    public void setAddressLive(String addressLive) {
        this.addressLive = addressLive;
    }

    public String getAddressRegistration() {
        return addressRegistration;
    }

    public void setAddressRegistration(String addressRegistration) {
        this.addressRegistration = addressRegistration;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public int getAgent() {
        return agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<User> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<User> roles) {
        this.roles = roles;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }


    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {

        if(this.photoBitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            BitmapDataObject bitmapDataObject = new BitmapDataObject();
            bitmapDataObject.imageByteArray = stream.toByteArray();
            out.writeObject(bitmapDataObject);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        if(this.photoBitmap!=null) {
            BitmapDataObject bitmapDataObject = (BitmapDataObject) in.readObject();
            this.photoBitmap = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);
        }
    }

}
