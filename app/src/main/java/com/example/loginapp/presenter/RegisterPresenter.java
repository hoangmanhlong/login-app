package com.example.loginapp.presenter;

import androidx.annotation.StringRes;

import com.example.loginapp.model.interator.RegisterInterator;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.utils.RegexUtils;
import com.example.loginapp.view.fragments.register.RegisterView;

public class RegisterPresenter implements RegisterListener {

    private RegisterInterator interator;

    private RegisterView view;

    private String email;

    private String password;

    private Boolean isEmailValid = false;

    private Boolean isPasswordValid = false;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        interator = new RegisterInterator(this);
    }

    public void clear() {
        view = null;
        interator.clear();
        interator = null;
        email = null;
        password = null;
        isPasswordValid = null;
        isEmailValid = null;
    }

    public void register() {
        view.isLoading(true);
        interator.register(email, password);
    }

    public void setEmail(String email) {
        isEmailValid = RegexUtils.isValidEmail(email);
        this.email = email;
        view.isValidEmail(isEmailValid);
        view.isRegisterButtonVisible(isRegisterButtonVisible());
    }

    public void setPassword(String password) {
        isPasswordValid = password.length() >= 6;
        this.password = password;
        view.isValidPassword(isPasswordValid);
        view.isRegisterButtonVisible(isRegisterButtonVisible());
    }

    private boolean isRegisterButtonVisible() {
        return isEmailValid && isPasswordValid;
    }

    @Override
    public void onSignupSuccess() {
        view.isLoading(false);
        view.signupSuccess();
    }

    @Override
    public void onSignupError(@StringRes int message) {
        view.isLoading(false);
        view.onMessage(message);
    }
}
