package org.ucomplex.ucomplex.Model.StudyStructure;

/**
 * Created by Sermilion on 01/05/16.
 */
public class UsersGroup {

    int id;
    String name;
    int year;
    int major;
    int specialism;
    int size;
    int study;
    int status;
    int client;

    public UsersGroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getSpecialism() {
        return specialism;
    }

    public void setSpecialism(int specialism) {
        this.specialism = specialism;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStudy() {
        return study;
    }

    public void setStudy(int study) {
        this.study = study;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }
}
