package com.example.loginapp.presenter;

import com.example.loginapp.model.Listener;
import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.RegisterActivity;

public class RegisterPresenter implements Listener {
    private RegisterActivity registerActivity;
    private UserInterator userInterator;

    public RegisterPresenter(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
        userInterator = new UserInterator();
    }

    public void register(String email, String password, String confirmPassword) {
        userInterator.registerAccount(registerActivity, email, password, confirmPassword, this);
    }

    public void goLoginScreen() {
        registerActivity.goLoginScreen();
    }


    @Override
    public void onSuccess(String message) {
        registerActivity.showSuccess(message);
    }

    @Override
    public void onFail(String message) {
        registerActivity.showFail(message);
    }
}
