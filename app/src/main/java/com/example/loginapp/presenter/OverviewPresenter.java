package com.example.loginapp.presenter;

import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.fragment.OverviewFragment;

public class OverviewPresenter {
    private OverviewFragment overviewFragment;

    private UserInterator userInterator;

    public OverviewPresenter(OverviewFragment overviewFragment) {
        this.overviewFragment = overviewFragment;
        userInterator = new UserInterator();
        userIsExist();
    }

    public void userIsExist() {
        userInterator.isLogged(overviewFragment.getContext(), this);
    }

    public void goHomeScreen() {
        overviewFragment.goHomeScreen();
    }
}
