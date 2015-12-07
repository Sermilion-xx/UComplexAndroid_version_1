package org.ucomplex.ucomplex.Model.Users;

import org.ucomplex.ucomplex.Model.Role;
import org.ucomplex.ucomplex.Model.StudyStructure.Progress;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sermilion on 04/12/2015.
 */
public class Student extends User implements Serializable{
    private String session;
    private String person;
    private Progress progress;


    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Student(){

    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

}
