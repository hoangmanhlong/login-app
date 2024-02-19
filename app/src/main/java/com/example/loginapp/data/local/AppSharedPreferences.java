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

    public void saveViewedStatus(String key, Boolean status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, status);
        editor.apply();
    }

    public boolean getNumberOfProductInBasket() {
        return preferences.getBoolean(Constant.IS_VIEWED_SHOPPING_CART_KEY, false);
    }

    public boolean getNumberOfProductInWishlist() {
        return preferences.getBoolean(Constant.IS_VIEWED_FAVORITES_LIST_KEY, false);
    }
}
