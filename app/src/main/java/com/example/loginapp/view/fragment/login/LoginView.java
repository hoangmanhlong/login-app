package com.example.loginapp.view.fragment.login;

import android.app.Activity;

public interface LoginView {
    void goHomeScreen();

    void onLoginMessage(String message);

    void onShowProcessBar(Boolean show);

    void isLoginFailed(boolean check);
}
