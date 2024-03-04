package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface CheckoutInfoListener {
    void onMessage(String message);

    void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses);

    void isDeliveryAddressesEmpty();
}
