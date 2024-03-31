package com.example.loginapp.model.interator;

import android.util.Log;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.HashMap;

public class RegisterInterator {

    private static final String TAG = RegisterInterator.class.getSimpleName();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RegisterListener listener;

    public RegisterInterator(RegisterListener listener) {
        this.listener = listener;
    }

    public void clear() {
        mAuth = null;
        listener = null;
    }

    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getUid();
                    HashMap<String, Object> updates = new HashMap<>();
                    updates.put(UserData.USERID, uid);
                    updates.put(UserData.EMAIL, email);
                    if (uid != null) {
                        Constant.userRef.child(uid)
                                .updateChildren(updates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        listener.onSignupSuccess();
                                        Log.d(TAG, "User registration successful.");
                                    } else {
                                        listener.onSignupError(R.string.server_error);
                                    }
                                });
                    }

                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        listener.onSignupError(R.string.account_already_exists);  // Dedicated error handling
                    } else {
                        listener.onSignupError(R.string.server_error);  // Dedicated error handling
                    }
                    Log.e(TAG, "User registration failed: " + e.getMessage(), e);  // Detailed error logging
                });
    }
}

