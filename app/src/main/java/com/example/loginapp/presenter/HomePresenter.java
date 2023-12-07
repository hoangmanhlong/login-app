package com.example.loginapp.presenter;

import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.HomeActivity;

public class HomePresenter {
    private HomeActivity homeActivity;
    private UserInterator userInterator;

    public HomePresenter(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        userInterator = new UserInterator();
    }

    public void logout() {
        userInterator.logout(homeActivity, this);
    }

    public void goLoginScreen() {
        homeActivity.goLogoutScreen();
    }
}
