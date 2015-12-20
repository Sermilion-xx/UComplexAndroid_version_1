package org.ucomplex.ucomplex.Model.Calendar;

import java.io.Serializable;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class CalendarEvent implements Serializable{

    private int day;
    private String descr;
    private String start;
    private String till;
    private int holiday;

    public CalendarEvent() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTill() {
        return till;
    }

    public void setTill(String till) {
        this.till = till;
    }

    public int isHoliday() {
        return holiday;
    }

    public void setHoliday(int holiday) {
        this.holiday = holiday;
    }
}
