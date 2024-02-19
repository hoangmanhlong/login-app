package com.example.loginapp.model.interator;

import android.app.Activity;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterInterator {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final RegisterListener listener;

    public RegisterInterator(RegisterListener listener) {
        this.listener = listener;
    }

    public void register(String email, String password, Activity activity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                            if (task.isSuccessful()) {
                                String uid = mAuth.getUid();
                                Constant.userRef.child(uid).setValue(new UserData(uid, email, password))
                                        .addOnCompleteListener(s -> listener.isSignupSuccess(true))
                                        .addOnFailureListener(e -> listener.isSignupSuccess(false));
                            } else {
                                listener.isSignupSuccess(false);
                            }
                        }
                );
    }

}

