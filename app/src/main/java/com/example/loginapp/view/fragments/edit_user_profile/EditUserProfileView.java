package com.example.loginapp.view.fragments.edit_user_profile;

import android.net.Uri;

public interface EditUserProfileView {

    void onMessage(String message);

    void showProcessBar(Boolean show);

    void onNavigateUp();

    void bindPhotoSelected(Uri photoUri);
}
