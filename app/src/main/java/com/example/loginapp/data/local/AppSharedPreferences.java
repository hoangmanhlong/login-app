package com.example.loginapp.data.local;


import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {

    private static final String ACCOUNT_PREFERENCES_NAME = "NOTIFICATION_NEW_PRODUCT";

    private static final String BASKET_KEY_NAME = "basket";

    private static final String WISHLIST_KEY_NAME = "wishlist";

    private static AppSharedPreferences instance;

    private final SharedPreferences preferences;

    private AppSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new AppSharedPreferences(context);
        }
        return instance;
    }

    public void saveNumberOfProductInBasket(int number, Boolean isBasket) {
        SharedPreferences.Editor editor = preferences.edit();
        if (isBasket) editor.putInt(BASKET_KEY_NAME, number);
        else editor.putInt(WISHLIST_KEY_NAME, number);
        editor.apply();
    }

    public int getNumberOfProductInBasket() {
        return preferences.getInt(BASKET_KEY_NAME, -1);
    }

    public int getNumberOfProductInWishlist() {
        return preferences.getInt(WISHLIST_KEY_NAME, -1);
    }
}
