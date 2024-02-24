package com.example.loginapp.presenter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.EditUserProfileInterator;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.view.fragments.edit_user_profile.EditUserProfileView;

public class EditUserProfilePresenter implements EditUserProfileListener {

    private final EditUserProfileInterator interator = new EditUserProfileInterator(this);

    private final EditUserProfileView view;

    @Nullable private Uri photoUri = null;

    private UserData userData;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        view.bindUserData(userData);
    }

    public EditUserProfilePresenter(EditUserProfileView view) {
        this.view = view;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
        view.bindPhotoSelected(photoUri);
    }

    public void saveUserData(String username, String phoneNumber, String address) {
        view.showProcessBar(true);
        interator.uploadImageToFirebase(photoUri, username, phoneNumber, address);
    }

    @Override
    public void isUpdateSuccess(boolean success) {
        view.showProcessBar(false);
        if (success) view.onNavigateUp();
        else view.onMessage("Error");
    }
}
