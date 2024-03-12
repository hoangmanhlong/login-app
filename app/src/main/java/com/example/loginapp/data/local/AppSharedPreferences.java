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

    public void saveViewedFavoritesListStatus(Boolean viewed) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constant.IS_VIEWED_FAVORITES_LIST_KEY, viewed);
        editor.apply();
    }

    public boolean getFavoritesListStatus() {
        return preferences.getBoolean(Constant.IS_VIEWED_FAVORITES_LIST_KEY, false);
    }

    public void setLanguage(boolean isVietnamese) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constant.IS_VIETNAMESE_LANGUAGE, isVietnamese);
        editor.apply();
    }

    public boolean getLanguage() {
        return preferences.getBoolean(Constant.IS_VIETNAMESE_LANGUAGE, false);
    }

//    public void usedForTheFirstTime() {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(Constant.IS_FIRST_TIME, true);
//        editor.apply();
//    }
//
//    public boolean usedForTheFirstTime() {
//
//    }
}