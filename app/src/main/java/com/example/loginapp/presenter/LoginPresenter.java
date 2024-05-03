package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.utils.RegexUtils;
import com.example.loginapp.view.fragments.login.LoginView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {

    private final String TAG = this.toString();

    private LoginView view;

    private String phoneNumber;

    // holder email input
    private String email;

    private String password;

    private boolean isEmailValid = false;

    private boolean isPasswordValid = false;

    private boolean isPhoneNumberValid = false;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public void clear() {
        email = null;
        password = null;
        phoneNumber = null;
        view = null;
    }

    public void initData() {
        view.loginEmailAndPasswordButtonEnabled(isEmailAndPasswordValid());
        view.requestOTPButtonEnabled(isPhoneNumberValid);
    }

    public void loginWithEmail() {
        if (RegexUtils.isValidEmail(email) && password.length() >= 6) {
            view.isLoading(true);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(task -> view.isLoginSuccess(true))
                    .addOnFailureListener(e -> {
                        view.isLoginSuccess(false);
                        Log.e(TAG, "loginWithEmail: " + e.getMessage());
                    });
        } else {
            view.isLoginSuccess(false);
        }
    }

    public void setEmail(String email) {
        this.email = email;
        isEmailValid = !email.isEmpty();
        view.loginEmailAndPasswordButtonEnabled(isEmailAndPasswordValid());
    }

    public void setPassword(String password) {
        this.password = password;
        isPasswordValid = !password.isEmpty();
        view.loginEmailAndPasswordButtonEnabled(isEmailAndPasswordValid());
    }

    private boolean isEmailAndPasswordValid() {
        return isEmailValid && isPasswordValid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        isPhoneNumberValid = phoneNumber.length() == 10 && RegexUtils.isNumber(phoneNumber);
        view.requestOTPButtonEnabled(isPhoneNumberValid);
    }
}
