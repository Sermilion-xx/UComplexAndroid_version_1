package org.ucomplex.ucomplex.Model.Calendar;

import org.ucomplex.ucomplex.Model.Users.Teacher;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class Timetable {
    private HashMap<String, String> teachers = new HashMap<>();
    private HashMap<String, String> groups = new HashMap<>();
    private HashMap<String, String> hours = new HashMap<>();
    private HashMap<String, String> rooms = new HashMap<>();
    private HashMap<String, String> subjects = new HashMap<>();
    private ArrayList<HashMap<String, String>> entries = new ArrayList<>();

    public Timetable() {
    }

    public HashMap<String, String> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String, String> groups) {
        this.groups = groups;
    }

    public void addEntry(HashMap<String, String> entries){
        this.entries.add(entries);
    }

    public ArrayList<HashMap<String, String>> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<HashMap<String, String>> entries) {
        this.entries = entries;
    }

    public HashMap<String, String> getTeachers() {
        return teachers;
    }

    public void setTeachers(HashMap<String, String> teachers) {
        this.teachers = teachers;
    }

    public HashMap<String, String> getHours() {
        return hours;
    }

    public void setHours(HashMap<String, String> hours) {
        this.hours = hours;
    }

    public HashMap<String, String> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, String> rooms) {
        this.rooms = rooms;
    }

    public HashMap<String, String> getSubjects() {
        return subjects;
    }

    public void setSubjects(HashMap<String, String> subjects) {
        this.subjects = subjects;
    }
}
