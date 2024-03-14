package com.example.loginapp.presenter;

import com.example.loginapp.view.fragments.login.LoginView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPresenter {

    private final String TAG = this.toString();

    private final LoginView view;

    private String phoneNumber;

    private String email;

    private String password;

    private boolean isEmailValid = false;

    private boolean isPasswordValid = false;

    private boolean isPhoneNumberValid = false;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public void initData() {
        view.loginEmailAndPasswordButtonEnabled(isEmailAndPasswordValid());
        view.requestOTPButtonEnabled(isPhoneNumberValid);
    }

    public void loginWithEmail() {
        if (isValidEmail(email) && password.length() >= 6) {
            view.isLoading(true);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(task -> view.isLoginSuccess(true))
                    .addOnFailureListener(e -> view.isLoginSuccess(false));
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
        isPhoneNumberValid = phoneNumber.length() == 10 && isNumber(phoneNumber);
        view.requestOTPButtonEnabled(isPhoneNumberValid);
    }

    private boolean isValidEmail(String email) {
        String emailRegex =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\" +
                        ".[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
