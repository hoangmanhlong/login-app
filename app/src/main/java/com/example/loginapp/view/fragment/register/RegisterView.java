package com.example.loginapp.view.fragment.register;

public interface RegisterView {
    void goLoginScreen();

    void onRegisterMessage(String message);

    void onShowProcessBar(Boolean show);
}
