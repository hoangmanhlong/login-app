package com.example.loginapp.model;

import com.example.loginapp.presenter.AppPresenter;

public class UserInterator {
    public void validateInfo(String email, String password, AppPresenter appPresenter) {
        boolean check = email.equals("long") && password.equals("123");
        if (check) {
            appPresenter.onSuccess();
        } else {
            appPresenter.onFail();
        }
    }
}
