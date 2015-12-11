package org.ucomplex.ucomplex.Model.StudyStructure;

import org.ucomplex.ucomplex.Model.Users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class Course implements Serializable {
    private int id;
    private int course;
    private int group;
    private int table;
    private int client;
    private int course_id;
    private ArrayList<Teacher> teachers;
    private ArrayList<File> files;
    private Department department;
    private Progress progress;
    private String name;
    private String description;

    public Course(){
        this.teachers = new ArrayList<>();
        this.files = new ArrayList<>();
        this.department = new Department();
        this.progress = new Progress();
    }



    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public Progress getProgress() {
        return progress;
    }

    public void addFile(File file){
        if(this.files == null){
            this.files = new ArrayList<>();
        }
        this.files.add(file);
    }

    public File getFile(int index){
        return this.files.get(index);
    }

    public void removeFile(int index){
//        this.files.remove(index);
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Department getDepartment() {
            return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void addTeacher(Teacher teacher){
        if(this.teachers==null){
            this.teachers = new ArrayList<>();
        }
        this.teachers.add(teacher);
    }

    public Teacher getTeacher(int index){
        return this.teachers.get(index);
    }

    public void removieTeachers(int index){
        this.teachers.remove(index);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}