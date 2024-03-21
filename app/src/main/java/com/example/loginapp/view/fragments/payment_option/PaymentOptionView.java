package com.example.loginapp.view.fragments.payment_option;

public interface PaymentOptionView {

    void setView(String merchandiseSubtotal, String shippingCost,String reducedPrice, String totalPayment);

    void goOrderSuccessScreen();

    void isLoading(Boolean loading);

    void onMessage(String message);

    void hasVoucher(boolean hasVoucher);
}
