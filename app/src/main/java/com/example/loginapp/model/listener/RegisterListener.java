package com.example.loginapp.model.listener;

import androidx.annotation.StringRes;

public interface RegisterListener {
    void isSignupSuccess();

    void onMessage(@StringRes int message);
}
