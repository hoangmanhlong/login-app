package com.example.loginapp.presenter;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.EditUserProfileInteractor;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.view.fragments.edit_user_profile.EditUserProfileView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class EditUserProfilePresenter implements EditUserProfileListener {

    private static final String TAG = EditUserProfilePresenter.class.getSimpleName();

    private EditUserProfileInteractor interactor;

    private EditUserProfileView view;

    @Nullable
    private Uri photoUri = null;

    private UserData userData;

    private Boolean isNameValid = false;

    private Boolean isPhoneNumberValid = false;

    private Boolean isAddressValid = false;

    public EditUserProfilePresenter(EditUserProfileView view) {
        this.view = view;
        userData = new UserData();
        userData.setUid(FirebaseAuth.getInstance().getUid());
        interactor = new EditUserProfileInteractor(this);
    }

    public void clear() {
        userData = null;
        view = null;
        photoUri = null;
        isAddressValid = null;
        isNameValid = null;
        isPhoneNumberValid = null;
        interactor.clear();
        interactor = null;
    }

    public void setUserData(UserData userData) {
        this.userData.copy(userData);
        view.bindUserData(this.userData);
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
        interactor.saveUserData(photoUri, userData);
    }

    @Override
    public void isUpdateSuccess(boolean success) {
        view.showProcessBar(false);
        if (success) view.onNavigateUp();
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
