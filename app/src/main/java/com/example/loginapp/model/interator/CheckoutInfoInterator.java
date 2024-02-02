package com.example.loginapp.model.interator;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.listener.CheckoutInfoListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class CheckoutInfoInterator {

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private CheckoutInfoListener listener;

    public CheckoutInfoInterator(CheckoutInfoListener listener) {
        this.listener = listener;
    }
}
