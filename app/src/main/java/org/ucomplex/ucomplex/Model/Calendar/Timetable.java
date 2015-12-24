package org.ucomplex.ucomplex.Model.Calendar;

import org.ucomplex.ucomplex.Model.Users.Teacher;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class Timetable {
    private ArrayList<HashMap<String, String>> teachers = new ArrayList<>();
    private ArrayList<HashMap<String, String>> hours = new ArrayList<>();
    private ArrayList<HashMap<String, String>> rooms = new ArrayList<>();
    private ArrayList<HashMap<String, String>> subjects = new ArrayList<>();
    private ArrayList<HashMap<String, String>> entries = new ArrayList<>();

    public Timetable() {
    }

    public void addEntry(HashMap<String, String> entries){
        this.entries.add(entries);
    }

    public void addTeacher(HashMap<String, String> teacher){
        this.teachers.add(teacher);
    }

    public void addHour(HashMap<String, String> hours){
        this.hours.add(hours);
    }

    public void addRoom(HashMap<String, String> rooms){
        this.rooms.add(rooms);
    }

    public void addSubject(HashMap<String, String> subjects){
        this.subjects.add(subjects);
    }


    public ArrayList<HashMap<String, String>> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<HashMap<String, String>> entries) {
        this.entries = entries;
    }

    public ArrayList<HashMap<String, String>> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<HashMap<String, String>> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<HashMap<String, String>> getHours() {
        return hours;
    }

    public void setHours(ArrayList<HashMap<String, String>> hours) {
        this.hours = hours;
    }

    public ArrayList<HashMap<String, String>> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<HashMap<String, String>> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<HashMap<String, String>> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<HashMap<String, String>> subjects) {
        this.subjects = subjects;
    }
}
