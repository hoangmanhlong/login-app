package com.example.loginapp.view.fragments.checkout;

import com.example.loginapp.model.entity.DeliveryAddress;

public interface CheckoutInfoView {

    void isLoading(boolean isLoading);

    void onMessage(String message);

    void bindDefaultDeliveryAddress(DeliveryAddress deliveryAddress);

}
