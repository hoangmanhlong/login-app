package com.example.loginapp.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.loginapp.model.Account;

import org.jetbrains.annotations.Nullable;

public class AppSharedPreferences {

    private static final String ACCOUNT_PREFERENCES_NAME = "account_preferences";
    private static final String USER_EMAIL_NAME = "user_email_name";
    private static final String USER_PASSWORD_NAME = "user_password_name";
    private static final String IS_LOGGED_NAME = "is_logged_name";

    private static AppSharedPreferences instance;

    private SharedPreferences preferences;

    private AppSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new AppSharedPreferences(context);
        }
        return instance;
    }

    public void saveUserAccount(String email, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_EMAIL_NAME, email);
        editor.putString(USER_PASSWORD_NAME, password);
        editor.apply();
    }
    @Nullable
    public Account getUserInfo() {
        try {
            String email = preferences.getString(USER_EMAIL_NAME, null);
            String password = preferences.getString(USER_PASSWORD_NAME, null);
            if (email != null && password != null) {
                return new Account(email, password);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void setLoginStatus(Boolean logged) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGGED_NAME, logged);
        editor.apply();
    }

    public boolean getLoginStatus() {
        return preferences.getBoolean(IS_LOGGED_NAME, false);
    }
}
