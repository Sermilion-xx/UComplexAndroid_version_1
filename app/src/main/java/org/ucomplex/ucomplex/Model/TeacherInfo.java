package org.ucomplex.ucomplex.Model;

import java.util.ArrayList;

/**
 * Created by Sermilion on 30/04/16.
 */
public class TeacherInfo {
    int id;
    String name;
    int type;
    int closed;
    String alias;
    int agent;
    int online;
    int department;
    String upqualification;
    int rank;
    String courses;
    int degree;
    String bio;
    int plan;
    int fact;
    double activity;
    String departmentName;
    String facultyName;
    ArrayList<TeacherTimetableCourses> teacherTimetableCourses = new ArrayList<>();


    public TeacherInfo() {
    }

    public ArrayList<TeacherTimetableCourses> getTeacherTimetableCourses() {
        return teacherTimetableCourses;
    }

    public void setTeacherTimetableCourses(ArrayList<TeacherTimetableCourses> teacherTimetableCourses) {
        this.teacherTimetableCourses = teacherTimetableCourses;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getUpqualification() {
        return upqualification;
    }

    public void setUpqualification(String upqualification) {
        if(upqualification.equals("") || upqualification.equals("")){
            this.upqualification = "не указанно";
        }else{
            this.upqualification = upqualification;
        }

    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        if(courses.equals("")){
            this.courses = "не указанно";
        }else{
            this.courses = courses;
        }

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
        if(bio.equals("")){
            this.bio = "не указанно";
        }else{
            this.bio = bio;
        }
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
}
