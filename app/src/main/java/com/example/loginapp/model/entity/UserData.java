package com.example.loginapp.model.entity;

public class UserData {

    private String uid;
    private String email;
    private String password;

    private String username;

    private String phoneNumber;

    private String address;

    private String photoUrl;

    public UserData(String username, String phoneNumber, String address, String photoUrl) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public UserData(
        String email,
        String username,
        String phoneNumber,
        String address,
        String photoUrl
    ) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public UserData(String uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        // Các thuộc tính còn lại sẽ được khởi tạo mặc định
        this.username = "";
        this.phoneNumber = "";
        this.address = "";
        this.photoUrl = "";
    }

    public UserData() {}

    public UserData(String username, String photoUrl) {
        this.username = username;
        this.photoUrl = photoUrl;
    }

    public UserData(
        String uid,
        String email,
        String password,
        String username,
        String phoneNumber,
        String address,
        String photoUrl
    ) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public static final String USERNAME = "username";

    public static final String PHONE_NUMBER = "phoneNumber";

    public static final String ADDRESS = "address";

    public static final String PHOTO_URL = "photoUrl";
}
