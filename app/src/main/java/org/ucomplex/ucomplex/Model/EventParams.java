package org.ucomplex.ucomplex.Model;

import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Sermilion on 02/12/2015.
 */
public class EventParams  implements Serializable {
    private String name;
    private int id;
    private int photo;
    private String code;
    private int gcourse;
    private String courseName;
    private int hourType;
    private int type;

    public EventParams(String name, int id, int photo, String code, int gcourse, String courseName, int hourType) {
        this.name = name;
        this.id = id;
        this.photo = photo;
        this.code = code;
        this.gcourse = gcourse;
        this.courseName = courseName;
        this.hourType = hourType;
    }

    public EventParams(String name, int id, int photo, String code, int gcourse, String courseName, int hourType, int type) {
        this.name = name;
        this.id = id;
        this.photo = photo;
        this.code = code;
        this.gcourse = gcourse;
        this.courseName = courseName;
        this.hourType = hourType;
        this.type = type;
    }

    public EventParams(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGcourse() {
        return gcourse;
    }

    public void setGcourse(int gcourse) {
        this.gcourse = gcourse;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getHourType() {
        return hourType;
    }

    public void setHourType(int hourType) {
        this.hourType = hourType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /** Included for serialization - write this layer to the output stream. */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.name);
        out.writeObject(this.code);
        out.writeObject(this.courseName);
        out.writeInt(this.gcourse);
        out.writeInt(this.hourType);
        out.writeInt(this.id);
        out.writeInt(this.photo);
        out.writeInt(this.type);
    }

    /** Included for serialization - read this object from the supplied input stream. */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        this.name = (String)in.readObject();
        this.code = (String)in.readObject();
        this.courseName = (String)in.readObject();
        this.gcourse = in.readInt();
        this.hourType = in.readInt();
        this.id = in.readInt();
        this.photo = in.readInt();
        this.type = in.readInt();
    }


}
