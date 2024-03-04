package com.example.loginapp;

import android.app.Application;

import com.example.loginapp.data.local.room.AppDatabase;

public class App extends Application {

    private static Application instance;

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getDatabase(this);
    }

    public static AppDatabase getDatabase() { return database; }

    public static Application getInstance() {
        return instance;
    }
}
