package com.example.loginapp.data;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppSharedPreferences {
//
//    private static final String ACCOUNT_PREFERENCES_NAME = "account_preferences";
//    private static final String ACCOUNT_KEY = "accounts";
//    private static final String IS_LOGGED_NAME = "is_logged_name";
//
//    private static AppSharedPreferences instance;
//
//    private final SharedPreferences preferences;
//
//    private AppSharedPreferences(Context context) {
//        preferences = context.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, Context.MODE_PRIVATE);
//    }
//
//    public static synchronized AppSharedPreferences getInstance(Context context) {
//        if (instance == null) {
//            instance = new AppSharedPreferences(context);
//        }
//        return instance;
//    }
//
//    public void saveUserAccount(String email, String password) {
//        List<Account> accounts = getAccounts();
//        accounts.add(new Account(email, password));
//        String updatedAccountsJson = new Gson().toJson(accounts);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(ACCOUNT_KEY, updatedAccountsJson);
//        editor.apply();
//    }
//
//    @Nullable
//    public List<Account> getAccounts() {
//        try {
//            String accountsJson = preferences.getString(ACCOUNT_KEY, "[]");
//            List<Account> accountList =
//                new ArrayList<>(Arrays.asList(new Gson().fromJson(accountsJson, Account[].class)));
//            if (!accountsJson.isEmpty()) {
//                return accountList;
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public void setLoginStatus(Boolean logged) {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(IS_LOGGED_NAME, logged);
//        editor.apply();
//    }
//
//    public boolean getLoginStatus() {
//        return preferences.getBoolean(IS_LOGGED_NAME, false);
//    }
}
