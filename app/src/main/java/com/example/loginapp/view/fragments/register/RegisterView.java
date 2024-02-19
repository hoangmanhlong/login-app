package com.example.loginapp.view.fragments.register;

public interface RegisterView {

    void goLoginScreen(String email, String password);

    void onMessage(String message);

    void isLoading(Boolean loading);
}
