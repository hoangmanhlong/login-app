package com.example.loginapp.presenter;

import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.MainActivity;

public class MainPresenter {
    private MainActivity mainActivity;

    private UserInterator userInterator;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        userInterator = new UserInterator();
    }

    public void UserIsExist() {
        userInterator.hasUser(mainActivity, this);
    }

    public void goHomeScreen() {
        mainActivity.goHomeScreen();
    }
}
