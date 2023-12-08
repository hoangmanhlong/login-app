package com.example.loginapp.presenter;

import com.example.loginapp.model.Listener;
import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.LoginActivity;

public class LoginPresenter implements Listener {
    private LoginActivity loginActivity;
    private UserInterator userInterator;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        userInterator = new UserInterator();
    }

    public void goHomeScreen() {
        loginActivity.goHomeScreen();
    }

    public void putUserInput(String email, String password) {
        userInterator.login(loginActivity, email, password, this);
    }

//    public void automaticallyUserInfo() {
//        userInterator.fillUserInfo(loginActivity, this);
//    }
//
//    public void fillInfo(String email, String password) {
//        loginActivity.setUserAccount(email, password);
//    }

    @Override
    public void onSuccess(String message) {
        loginActivity.showSuccess(message);
    }

    @Override
    public void onFail(String message) {
        loginActivity.showFail(message);
    }
}
