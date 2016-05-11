package org.ucomplex.ucomplex.Model.Calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class UCCalendar implements Serializable {

    private String method;
    private ArrayList<CalendarEvent> events = new ArrayList<>();
    private int ajax;
    private int subjId;
    private String year;
    private String month;
    private String day;
    private String pre_month;
    private String next_month;
    private String group;
    private String subgroup;
    private String course;
    private HashMap<String, String> courses = new HashMap<>();
    private ArrayList<ChangedDay> changedDays = new ArrayList<>();
    private Timetable timetable = new Timetable();

    public UCCalendar() {
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void addEvent(CalendarEvent event){
        this.events.add(event);
    }


    public void addChangeDay(ChangedDay day){
        this.changedDays.add(day);
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<CalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<CalendarEvent> events) {
        this.events = events;
    }

    public int getAjax() {
        return ajax;
    }

    public void setAjax(int ajax) {
        this.ajax = ajax;
    }

    public int getSubjId() {
        return subjId;
    }

    public void setSubjId(int subjId) {
        this.subjId = subjId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPre_month() {
        return pre_month;
    }

    public void setPre_month(String pre_month) {
        this.pre_month = pre_month;
    }

    public String getNext_month() {
        return next_month;
    }

    public void setNext_month(String next_month) {
        this.next_month = next_month;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public HashMap<String, String> getCourses() {
        return courses;
    }

    public void setCourses(HashMap<String, String> courses) {
        this.courses = courses;
    }

    public ArrayList<ChangedDay> getChangedDays() {
        return changedDays;
    }

    public void setChangedDays(ArrayList<ChangedDay> changedDays) {
        this.changedDays = changedDays;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
}
