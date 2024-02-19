package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.model.interator.VerificationInterator;
import com.example.loginapp.model.listener.VerificationListener;
import com.example.loginapp.view.fragments.verification.VerificationView;

public class VerificationPresenter implements VerificationListener {

    private final VerificationInterator interator = new VerificationInterator(this);

    private final VerificationView view;

    public VerificationPresenter(VerificationView view) {
        this.view = view;
    }

    public void startPhoneNumberVerification(String phoneNumber, Activity activity) {
        view.isLoading(true);
        interator.startPhoneNumberVerification(phoneNumber, activity);
    }

    public void verifyPhoneNumberWithCode(String code, Activity activity){
        interator.verifyPhoneNumberWithCode(code, activity);
    }

    @Override
    public void enableVerifyButton() {
        view.isLoading(false);
        view.enableVerifyButton();
    }

    @Override
    public void goHomeScreen() {
        view.goHomeScreen();
    }

    public String formatPhoneNumber(String phoneNumber) {
        return "+84" + phoneNumber.substring(1);
    }
}
