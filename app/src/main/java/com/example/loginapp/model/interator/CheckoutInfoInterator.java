package com.example.loginapp.model.interator;

import com.example.loginapp.model.listener.CheckoutInfoListener;

public class CheckoutInfoInterator {
    private CheckoutInfoListener listener;

    public CheckoutInfoInterator(CheckoutInfoListener listener) {
        this.listener = listener;
    }
}
