package com.example.loginapp.presenter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.EditUserProfileInterator;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.view.fragments.edit_user_profile.EditUserProfileView;

import java.util.regex.Pattern;

public class EditUserProfilePresenter implements EditUserProfileListener {

    private final EditUserProfileInterator interator = new EditUserProfileInterator(this);

    private final EditUserProfileView view;

    @Nullable private Uri photoUri = null;

    private UserData userData;

    private String username;

    private String phoneNumber;

    private String address;

    private boolean isNameValid = false;

    private boolean isPhoneNumberValid = false;

    private boolean isAddressValid = false;

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

    public void setUsername(String username) {
        this.username = username;
        isNameValid = !username.isEmpty();
        if (isNameValid) userData.setUsername(username);
        view.saveButtonEnabled(isInputValid());
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        isPhoneNumberValid = phoneNumber.length() == 10 && isNumber(phoneNumber);
        if (isPhoneNumberValid) userData.setPhoneNumber(phoneNumber);
        view.saveButtonEnabled(isInputValid());
    }

    public void setAddress(String address) {
        this.address = address;
        isAddressValid = !address.isEmpty();
        if (isAddressValid) userData.setAddress(address);
        view.saveButtonEnabled(isInputValid());
    }

    private boolean isInputValid() {
        return isNameValid && isPhoneNumberValid && isAddressValid;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
        if (photoUri != null) view.bindPhotoSelected(photoUri);
        else view.onMessage("No file selected");
    }

    public void saveUserData() {
        view.showProcessBar(true);
        interator.saveUserData(photoUri, userData);
    }

    @Override
    public void isUpdateSuccess(boolean success) {
        view.showProcessBar(false);
        if (success) view.onNavigateUp();
        else view.onMessage("Error");
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
