package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.model.interator.RegisterInterator;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.view.fragment.register.RegisterView;

public class RegisterPresenter implements RegisterListener {
    private RegisterInterator interator;
    private RegisterView view;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        interator = new RegisterInterator(this);
    }

    public void register(String email, String password, String confirmPassword, Activity activity) {
        interator.register(email, password, activity, confirmPassword);
    }

    public void goLoginScreen() {
        view.goLoginScreen();
    }

    @Override
    public void onRegisterMessage(String message) {
        view.onRegisterMessage(message);
    }

    @Override
    public void onShowProcessBar(Boolean show) {
        view.onShowProcessBar(show);
    }
}
