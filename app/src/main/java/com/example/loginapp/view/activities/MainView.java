package com.example.loginapp.view.activities;

public interface MainView {

    void notifyCartChanged(Boolean show);

    void notifyFavoriteChanged(Boolean show);

    void hasUser(Boolean hasUser);
}
