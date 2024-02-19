package com.example.loginapp.model.interator;

import android.app.Activity;

import com.example.loginapp.model.listener.LoginListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginInteractor {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final LoginListener listener;

    public LoginInteractor(LoginListener listener) {
        this.listener = listener;
    }

    public void loginWithEmail(String email, String password, Activity activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> listener.loginStatus(task.isSuccessful()));
    }
}
