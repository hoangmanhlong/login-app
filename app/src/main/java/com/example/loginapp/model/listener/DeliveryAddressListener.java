package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressListener {
    void getDeliveryAddress(List<DeliveryAddress> deliveryAddresses);

    void isDeliveryAddressEmpty();
}
