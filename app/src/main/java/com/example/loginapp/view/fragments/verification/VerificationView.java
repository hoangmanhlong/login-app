package com.example.loginapp.view.fragments.verification;

public interface VerificationView {
    void enableVerifyButton();

    void isLoading(Boolean check);

    void goHomeScreen();

    void bindPhoneNumber(String phoneNumber);
}
