package com.example.loginapp.model.listener;

import androidx.annotation.StringRes;

public interface RegisterListener {
    void onSignupSuccess();
    void onSignupError(@StringRes int message);
}
