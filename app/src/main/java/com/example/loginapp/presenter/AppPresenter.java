package com.example.loginapp.presenter;

import com.example.loginapp.model.OnLoginFinishedListener;
import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.LoginActivity;

public class AppPresenter implements OnLoginFinishedListener {
    private LoginActivity loginActivity;
    private UserInterator userInterator;

    public AppPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        userInterator = new UserInterator();
    }

    public void putUserInput(String email, String password) {
        userInterator.validateInfo(email, password, this);
    }

    @Override
    public void onSuccess() {
        loginActivity.showSuccess();
    }

    @Override
    public void onFail() {
        loginActivity.showFail();
    }
}
