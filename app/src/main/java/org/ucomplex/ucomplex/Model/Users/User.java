package org.ucomplex.ucomplex.Model.Users;

import org.ucomplex.ucomplex.Model.Role;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sermilion on 05/12/2015.
 */
public class User implements Serializable {
    private int id;
    private int client;
    private String name;
    private String login;
    private String pass;
    private String code;
    private int photo;
    private String phone;
    private String email;
    private ArrayList<Role> roles;

    public User(int id, int client, String name, String login, String pass, String code, int photo, String phone, String email, ArrayList<Role> roles) {
        this.id = id;
        this.client = client;
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.code = code;
        this.photo = photo;
        this.phone = phone;
        this.email = email;
        this.roles = roles;
    }

    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
}
