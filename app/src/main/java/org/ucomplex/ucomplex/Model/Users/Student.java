package org.ucomplex.ucomplex.Model.Users;

import org.ucomplex.ucomplex.Model.StudyStructure.Progress;

import java.io.Serializable;

/**
 * Created by Sermilion on 04/12/2015.
 */
public class Student extends User implements Serializable{
    private String session;
    private Progress progress;
    private String alias;


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


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }


}
