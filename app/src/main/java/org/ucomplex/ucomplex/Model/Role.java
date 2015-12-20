package org.ucomplex.ucomplex.Model;

import org.ucomplex.ucomplex.Model.Users.User;

import java.io.Serializable;

/**
 * Created by Sermilion on 04/12/2015.
 */
public class Role extends User implements Serializable{
    private int type;
    private String name;


    public Role(){

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
