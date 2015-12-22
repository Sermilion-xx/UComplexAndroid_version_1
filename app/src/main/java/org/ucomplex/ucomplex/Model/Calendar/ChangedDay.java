package org.ucomplex.ucomplex.Model.Calendar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class ChangedDay implements Serializable {
    private int day;
    private ArrayList<Lesson> lessons = new ArrayList<>();

    public ChangedDay() {
    }

    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }
}
