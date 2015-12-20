package org.ucomplex.ucomplex.Model.Calendar;

import java.io.Serializable;

/**
 * Created by Sermilion on 20/12/2015.
 */
public class Lesson implements Serializable {
    private int number;
    private int type;
    private int course;

    public Lesson() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }
}
