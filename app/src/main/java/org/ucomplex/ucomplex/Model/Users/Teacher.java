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
    private ArrayList<String> upqualification;
    private int degree;
    private String bio;
    private int plan;
    private int fact;
    private int fails;
    private String activity_update;
    private int selection;
    private Department department;
    private int closed;
    private int agent;
    private int online;
    private ArrayList<File> files;
    private Bitmap photoBitmap;


    public Teacher(){

    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
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

    public ArrayList<String> getUpqualification() {
        return upqualification;
    }

    public void setUpqualification(ArrayList<String> upqualification) {
        this.upqualification = upqualification;
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
}
