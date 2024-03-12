package com.example.loginapp.model.interator;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterInterator {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final RegisterListener listener;

    public RegisterInterator(RegisterListener listener) {
        this.listener = listener;
    }

    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Successful registration logic (move database saving logic here)
                    String uid = mAuth.getUid();
                    Constant.userRef.child(uid).setValue(new UserData(uid, email, password))
                            .addOnCompleteListener(s -> listener.isSignupSuccess())
                            .addOnFailureListener(e -> listener.onMessage(R.string.database_error)); // Use specific error message for database failures
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        listener.onMessage(R.string.account_already_exists);
                    } else {
                        listener.onMessage(R.string.server_error);
                    }
                });
    }

}

