package com.example.loginapp.presenter;

import android.app.Activity;
import android.util.Log;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.login.LoginView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPresenter {

    private final LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public void loginWithEmail(String email, String password, Activity activity) {
        if (isValidEmail(email) && password.length() >= 6) {
            view.isLoading(true);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, task -> view.isLoginSuccess(task.isSuccessful()))
                    .addOnFailureListener(e -> view.onMessage(e.getMessage()));
        } else {
            view.isLoginSuccess(false);
        }
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
}
