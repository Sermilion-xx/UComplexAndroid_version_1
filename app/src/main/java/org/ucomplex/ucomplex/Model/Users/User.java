package org.ucomplex.ucomplex.Model.Users;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.ucomplex.ucomplex.Model.EventParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private ArrayList<User> roles = new ArrayList<>();
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
    private boolean friendRequested;
    private int position;
    private String positionName;
    private boolean is_black;
    private boolean me_black;
    private boolean req_friend;
    private String encodedBitmap;

    public void setEncodedBitmap(String encodedBitmap) {
        this.encodedBitmap = encodedBitmap;
    }

    public void addRole(User role){
        roles.add(role);
    }

    public boolean is_black() {
        return is_black;
    }

    public boolean isMe_black() {
        return me_black;
    }

    public void setMe_black(boolean me_black) {
        this.me_black = me_black;
    }

    public boolean isReq_friend() {
        return req_friend;
    }

    public void setReq_friend(boolean req_friend) {
        this.req_friend = req_friend;
    }

    public boolean isIs_black() {
        return is_black;
    }

    public void setIs_black(boolean is_black) {
        this.is_black = is_black;
    }



    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFriendRequested() {
        return friendRequested;
    }

    public void setFriendRequested(boolean friendRequested) {
        this.friendRequested = friendRequested;
    }

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

    public ArrayList<User> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<User> roles) {
        this.roles = roles;
    }

    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {

        if(this.photoBitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            BitmapDataObject bitmapDataObject = new BitmapDataObject();
            bitmapDataObject.imageByteArray = stream.toByteArray();
            out.writeObject(bitmapDataObject);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        if(this.photoBitmap!=null) {
            BitmapDataObject bitmapDataObject = (BitmapDataObject) in.readObject();
            this.photoBitmap = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);
        }
    }

}
