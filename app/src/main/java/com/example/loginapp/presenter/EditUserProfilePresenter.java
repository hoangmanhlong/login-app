package com.example.loginapp.presenter;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.loginapp.model.interator.EditUserProfileInterator;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.view.fragment.edit_user_profile.EditUserProfileView;

public class EditUserProfilePresenter implements EditUserProfileListener {

    private final EditUserProfileInterator interator = new EditUserProfileInterator(this);

    private final EditUserProfileView view;

    public EditUserProfilePresenter(EditUserProfileView view) {
        this.view = view;
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    public void saveUserData(@NonNull Uri uri, String username, String phoneNumber, String address) {
        interator.uploadImageToFirebase(uri, username, phoneNumber, address);
    }

    @Override
    public void showProcessBar(Boolean show) {
        view.showProcessBar(show);
    }

    @Override
    public void goUserProfile() {
        view.onNavigateUp();
    }

}
