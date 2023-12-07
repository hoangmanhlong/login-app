package com.example.loginapp.data;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.loginapp.model.Account;

import org.jetbrains.annotations.Nullable;

public class AppSharedPreferences {
    private static final String ACCOUNT_PREFERENCES_NAME = "account_preferences";
    private static final String USER_EMAIL_NAME = "user_email_name";
    private static final String USER_PASSWORD_NAME = "user_password_name";

    public static void saveUserAccount(Context context, String email, String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER_EMAIL_NAME, email);
        editor.putString(USER_PASSWORD_NAME, password);
        editor.apply();
    }

    @Nullable
    public static Account getUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE);
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

    public static boolean hasUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String email = preferences.getString(USER_EMAIL_NAME, "");
        String password = preferences.getString(USER_PASSWORD_NAME, "");
        return !(email.equals("") || password.equals(""));
    }

    public static void clearUserAccount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
