package com.example.loginapp.view.fragments.login;

public interface LoginView {

    void isLoading(Boolean loading);

    void isLoginSuccess(boolean isSuccess);

    void onMessage(String message);

    void requestOTPButtonEnabled(boolean enabled);
}
