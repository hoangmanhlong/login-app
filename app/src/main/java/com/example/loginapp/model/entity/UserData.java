package com.example.loginapp.model.entity;

import java.io.Serializable;

public class UserData implements Serializable {

    private String uid;

    private String username;

    private String email;

    private String phoneNumber;

    private String address;

    private String avatar;

    public UserData(String uid, String username, String email, String phoneNumber, String address, String avatar) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.avatar = avatar;
    }

    public UserData() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatar() {
        return avatar;
    }

    public static final String USERNAME = "username";

    public static final String PHONE_NUMBER = "phoneNumber";

    public static final String ADDRESS = "address";

    public static final String AVATAR = "avatar";

    public static final String USERID = "uid";

    public static final String EMAIL = "email";

    public UserData copy(UserData userData) {
        this.uid = userData.uid;
        this.username = userData.username;
        this.email = userData.email;
        this.address = userData.address;
        this.phoneNumber = userData.phoneNumber;
        this.avatar = userData.avatar;
        return this;
    }
}
