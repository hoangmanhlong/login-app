package com.example.loginapp.presenter;

import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.fragment.HomeFragment;

public class HomePresenter {
    private HomeFragment homeFragment;
    private UserInterator userInterator;

    public HomePresenter(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
        userInterator = new UserInterator();
    }

    public void logout() {
        userInterator.logout(homeFragment.getContext(), this);
    }

    public void goLoginScreen() {
        homeFragment.goLogoutScreen();
    }
}
