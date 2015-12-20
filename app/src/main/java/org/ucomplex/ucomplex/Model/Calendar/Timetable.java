package org.ucomplex.ucomplex.Model.Calendar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class Timetable {
    private ArrayList<HashMap<Integer, String>> teachers;
    private ArrayList<HashMap<Integer, String>> hours;
    private ArrayList<HashMap<Integer, String>> rooms;
    private ArrayList<HashMap<Integer, String>> subjects;

    public Timetable() {
    }

    public ArrayList<HashMap<Integer, String>> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<HashMap<Integer, String>> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<HashMap<Integer, String>> getHours() {
        return hours;
    }

    public void setHours(ArrayList<HashMap<Integer, String>> hours) {
        this.hours = hours;
    }

    public ArrayList<HashMap<Integer, String>> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<HashMap<Integer, String>> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<HashMap<Integer, String>> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<HashMap<Integer, String>> subjects) {
        this.subjects = subjects;
    }
}
