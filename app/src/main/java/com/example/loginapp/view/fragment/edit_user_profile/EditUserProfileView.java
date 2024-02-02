package com.example.loginapp.view.fragment.edit_user_profile;

import com.example.loginapp.model.entity.UserData;

public interface EditUserProfileView {

    void onMessage(String message);

    void showProcessBar(Boolean show);

    void onNavigateUp();
}
