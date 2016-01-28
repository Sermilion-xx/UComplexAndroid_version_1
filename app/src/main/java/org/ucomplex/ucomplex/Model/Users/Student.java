package org.ucomplex.ucomplex.Model.Users;

import org.ucomplex.ucomplex.Model.StudyStructure.Progress;

import java.io.Serializable;

/**
 * Created by Sermilion on 04/12/2015.
 */
public class Student extends User implements Serializable{
    private Progress progress;
    private String alias;
    private int group;
    private int major;
    private int study;
    private int year;
    private int payment;
    private int contract_year;

    public int getContract_year() {
        return contract_year;
    }

    public void setContract_year(int contract_year) {
        this.contract_year = contract_year;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStudy() {
        return study;
    }

    public void setStudy(int study) {
        this.study = study;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }





}
