package com.example.loginapp.model.listener;

import android.app.Activity;

public interface LoginListener {
    void goHomeScreen();

    void isLoginFailed(boolean check);

    void onShowProcessBar(Boolean show);
}
