package org.ucomplex.ucomplex.Model.Users;

import android.graphics.Bitmap;

import org.ucomplex.ucomplex.Model.StudyStructure.Department;
import org.ucomplex.ucomplex.Model.StudyStructure.File;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class Teacher extends User implements Serializable {
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


    public Teacher(){

    }

    public String getAcademicAwards() {
        return academicAwards;
    }

    public void setAcademicAwards(String academicAwards) {
        this.academicAwards = academicAwards;
    }


    public String getStatuses() {
        return statuses;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public int getLead() {
        return lead;
    }

    public void setLead(int lead) {
        this.lead = lead;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }



    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public String getUpqualification() {
        return upqualification;
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


    public void setUpqualification(String upqualification) {
        this.upqualification = upqualification;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
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



}
