package com.example.loginapp.view.fragments.edit_user_profile;

import android.net.Uri;

import com.example.loginapp.model.entity.UserData;

public interface EditUserProfileView {

    void onMessage(String message);

    void showProcessBar(Boolean show);

    void onNavigateUp();

    void bindPhotoSelected(Uri photoUri);

    void bindUserData(UserData userData);

    void saveButtonEnabled(boolean enabled);

}
