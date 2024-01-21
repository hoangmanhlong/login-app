package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.UserData;

public interface EditUserProfileListener {

    void onMessage(String message);

    void showProcessBar(Boolean show);

    void goUserProfile();

    void getUserData(UserData userData);
}
