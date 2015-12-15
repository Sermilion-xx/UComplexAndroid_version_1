package org.ucomplex.ucomplex.Model.Users;

import android.graphics.Bitmap;

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
    private String birthday;
    private String login;
    private String pass;
    private String code;
    private int photo;
    private String phone;
    private String email;
    private ArrayList<Role> roles;
    private int person;
    private int agent;
    private int online;
    private Bitmap photoBitmap;
    private String birthplace;
    private int country;
    private String addressRegistration;
    private String addressLive;
    private String phoneWork;
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public User(){
        this.id = -1;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getAddressLive() {
        return addressLive;
    }

    public void setAddressLive(String addressLive) {
        this.addressLive = addressLive;
    }

    public String getAddressRegistration() {
        return addressRegistration;
    }

    public void setAddressRegistration(String addressRegistration) {
        this.addressRegistration = addressRegistration;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public int getAgent() {
        return agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
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
