package org.ucomplex.ucomplex.Model.Calendar;

import org.javatuples.Triplet;

import java.util.ArrayList;

/**
 * Created by Sermilion on 24/04/16.
 */
public class CalendarOneDay {

    private int subjId;
    private String date;
    private String time;
    private int hourNumber;
    private int subgroup;
    private int group;
    private int course;
    private int table;
    private int maxNumber;
    private int recordID;
    private int hourType;
    //id, mark, name
    private ArrayList<Triplet<Integer, Integer, String>> studentsProgress = new ArrayList<>();

    public CalendarOneDay() {
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getHourType() {
        return hourType;
    }

    public void setHourType(int hourType) {
        this.hourType = hourType;
    }

    public int getSubjId() {
        return subjId;
    }

    public void setSubjId(int subjId) {
        this.subjId = subjId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHourNumber() {
        return hourNumber;
    }

    public void setHourNumber(int hourNumber) {
        this.hourNumber = hourNumber;
    }

    public int getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(int subgroup) {
        this.subgroup = subgroup;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public ArrayList<Triplet<Integer, Integer, String>> getStudentsProgress() {
        return studentsProgress;
    }

    public void setStudentsProgress(ArrayList<Triplet<Integer, Integer, String>> studentsProgress) {
        this.studentsProgress = studentsProgress;
    }
}
