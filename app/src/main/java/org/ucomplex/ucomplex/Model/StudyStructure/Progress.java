package org.ucomplex.ucomplex.Model.StudyStructure;

import org.ucomplex.ucomplex.Model.Users.User;

import java.io.Serializable;

/**
 * Created by Sermilion on 06/12/2015.
 */
public class Progress implements Serializable{
    private int student;
    private Course course;
    private User teacher;
    private int table;
    private int time;
    private int mark;
    private int type;
    private int _mark;
    private int markCount;
    private int absence;
    private int individ;
    private int hours;

    public Progress(){

    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int get_mark() {
        return _mark;
    }

    public void set_mark(int _mark) {
        this._mark = _mark;
    }

    public int getMarkCount() {
        return markCount;
    }

    public void setMarkCount(int markCount) {
        this.markCount = markCount;
    }

    public int getAbsence() {
        return absence;
    }

    public void setAbsence(int absence) {
        this.absence = absence;
    }

    public int getIndivid() {
        return individ;
    }

    public void setIndivid(int individ) {
        this.individ = individ;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
