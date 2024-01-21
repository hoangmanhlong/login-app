package com.example.loginapp.model.listener;

public interface RegisterListener {
    void goLoginScreen();

    void onRegisterMessage(String message);

    void onShowProcessBar(Boolean show);
}
