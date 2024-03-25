package com.example.loginapp.presenter;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.EditUserProfileInterator;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.view.fragments.edit_user_profile.EditUserProfileView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class EditUserProfilePresenter implements EditUserProfileListener {

    private static final String TAG = EditUserProfilePresenter.class.getSimpleName();

    private final EditUserProfileInterator interator = new EditUserProfileInterator(this);

    private EditUserProfileView view;

    @Nullable
    private Uri photoUri = null;

    private final UserData userData;

    private boolean isNameValid = false;

    private boolean isPhoneNumberValid = false;

    private boolean isAddressValid = false;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData.copy(userData);
        view.bindUserData(this.userData);
    }

    public EditUserProfilePresenter(EditUserProfileView view) {
        this.view = view;
        userData = new UserData();
        userData.setUid(FirebaseAuth.getInstance().getUid());
    }

    public void setUsername(String username) {
        isNameValid = !username.isEmpty();
        if (isNameValid) userData.setUsername(username);
        view.saveButtonEnabled(isInputValid());
    }

    public void setPhoneNumber(String phoneNumber) {
        isPhoneNumberValid = phoneNumber.length() == 10 && isNumber(phoneNumber);
        if (isPhoneNumberValid) userData.setPhoneNumber(phoneNumber);
        view.saveButtonEnabled(isInputValid());
    }

    public void setAddress(String address) {
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

    public void detachView() {
        view = null;
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
