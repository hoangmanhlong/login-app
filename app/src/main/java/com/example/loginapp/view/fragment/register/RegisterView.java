package com.example.loginapp.view.fragment.register;

import android.util.Pair;

public interface RegisterView {

    void goLoginScreen(String email, String password);

    void onMessage(String message);

    void isLoading(Boolean loading);
}
