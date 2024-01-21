package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInterator;
import com.example.loginapp.model.listener.UserProfileListener;
import com.example.loginapp.view.fragment.user_profile.UserProfileView;

public class UserProfilePresenter implements UserProfileListener {
    private UserProfileView view;

    private UserProfileInterator interator;

    public UserProfilePresenter(UserProfileView view) {
        this.view = view;
        interator = new UserProfileInterator(this);
        interator.getUserData();
    }

    @Override
    public void getUserData(UserData userData) {
        view.getUserData(userData);
    }
}
