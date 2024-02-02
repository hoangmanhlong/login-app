package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInterator;
import com.example.loginapp.model.listener.UserProfileListener;
import com.example.loginapp.view.fragment.user_profile.UserProfileView;

public class UserProfilePresenter implements UserProfileListener {

    private final UserProfileView view;

    private final UserProfileInterator interator = new UserProfileInterator(this);

    public UserData userData;

    public UserProfilePresenter(UserProfileView view) {
        this.view = view;
    }

    public void getUserData() {
        interator.getUserData();
    }

    public void initData() {
        if (userData == null) getUserData();
        else view.getUserData(userData);
    }

    @Override
    public void getUserData(UserData userData) {
        view.getUserData(userData);
        this.userData = userData;
    }
}
