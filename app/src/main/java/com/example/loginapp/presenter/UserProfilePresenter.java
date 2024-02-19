package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInterator;
import com.example.loginapp.model.listener.UserProfileListener;
import com.example.loginapp.view.fragments.user_profile.UserProfileView;

public class UserProfilePresenter implements UserProfileListener {

    private final UserProfileView view;

    private final UserProfileInterator interator = new UserProfileInterator(this);

    private UserData currentUser;

    public UserData getCurrentUser() {
        return currentUser;
    }

    public UserProfilePresenter(UserProfileView view) {
        this.view = view;
    }

    public void getUserData() {
        interator.getUserData();
    }

    public void initData() {
        if (currentUser == null) getUserData();
        else view.bindUserData(currentUser);
    }

    @Override
    public void getUserData(UserData userData) {
        currentUser = userData;
        view.bindUserData(currentUser);
    }
}
