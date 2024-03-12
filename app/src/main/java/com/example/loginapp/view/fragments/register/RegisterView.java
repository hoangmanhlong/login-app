package com.example.loginapp.view.fragments.register;

import androidx.annotation.StringRes;

public interface RegisterView {

    void onMessage(@StringRes int message);

    void isLoading(Boolean loading);

    void isValidEmail(boolean isValid);

    void isValidPassword(boolean isValid);

    void isValidConfirmPassword(boolean isValid);

    void isRegisterButtonVisible(boolean visible);
}
