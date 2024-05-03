package com.example.loginapp.data.local;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.loginapp.utils.Constant;

public class AppSharedPreferences {

    private static AppSharedPreferences instance;

    private final SharedPreferences preferences;

    private AppSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(Constant.VIEWED_STATUS_KEY, Context.MODE_PRIVATE);
    }

    public static synchronized AppSharedPreferences getInstance(Context context) {
        if (instance == null) instance = new AppSharedPreferences(context);
        return instance;
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }
}