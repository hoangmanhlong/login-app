package com.example.loginapp.view.fragments.checkout;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;

public interface CheckoutInfoView {

    void isLoading(boolean isLoading);

    void onMessage(String message);

    void bindDeliveryAddress(DeliveryAddress deliveryAddress);

    void getSharedData();

    void isCheckoutButtonVisible(boolean visible);

    void isSelectDeliveryAddressButtonVisible(boolean visible);

    void navigateToPaymentMethodScreen(Order order);

    void isSaveAddressCheckboxVisible(boolean visible);

}
