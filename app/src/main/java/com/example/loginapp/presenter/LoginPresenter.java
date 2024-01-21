package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.model.interator.LoginInteractor;
import com.example.loginapp.model.listener.LoginListener;
import com.example.loginapp.view.fragment.login.LoginView;

public class LoginPresenter implements LoginListener {
    private final LoginView view;
    private final LoginInteractor loginInteractor;

    public LoginPresenter(LoginView view) {
        this.view = view;
        loginInteractor = new LoginInteractor(this);
    }

    @Override
    public void goHomeScreen() {
        view.goHomeScreen();
    }

    public void loginWithEmail(String email, String password, Activity activity) {
        loginInteractor.loginWithEmail(email, password, activity);
    }

    @Override
    public void isLoginFailed(boolean check) {
        view.isLoginFailed(check);
    }

    @Override
    public void onShowProcessBar(Boolean show) {
        view.onShowProcessBar(show);
    }
}
