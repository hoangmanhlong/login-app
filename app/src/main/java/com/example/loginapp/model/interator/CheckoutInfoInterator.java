package com.example.loginapp.model.interator;

import com.example.loginapp.model.listener.CheckoutInfoListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckoutInfoInterator {

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private CheckoutInfoListener listener;

    public CheckoutInfoInterator(CheckoutInfoListener listener) {
        this.listener = listener;
    }
}
