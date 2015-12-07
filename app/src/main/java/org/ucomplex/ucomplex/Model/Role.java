package org.ucomplex.ucomplex.Model;

import java.io.Serializable;

/**
 * Created by Sermilion on 04/12/2015.
 */
public class Role implements Serializable{
    private String id;
    private String person;
    private int type;
    private String name;

    public Role(String id, String person, int type, String name) {
        this.id = id;
        this.person = person;
        this.type = type;
        this.name = name;
    }

    public Role(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
