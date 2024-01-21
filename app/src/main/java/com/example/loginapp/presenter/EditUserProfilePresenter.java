package com.example.loginapp.presenter;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.EditUserProfileInterator;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.view.fragment.edit_user_profile.EditUserProfileView;

public class EditUserProfilePresenter implements EditUserProfileListener {

    private final EditUserProfileInterator interator = new EditUserProfileInterator(this);

    private final EditUserProfileView view;

    public EditUserProfilePresenter(EditUserProfileView view) {
        this.view = view;
    }

    public void getUserData() {
        interator.getUserData();
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
        view.goUserProfile();
    }

    @Override
    public void getUserData(UserData userData) {
        String name = userData.getUsername();
        String phone = userData.getPhoneNumber();
        String address = userData.getAddress();
        if (name == null) name = "";
        if (phone == null) phone = "";
        if (address == null) address = "";
        view.loadUserData(new UserData(name, phone, address, userData.getPhotoUrl()));
    }
}
