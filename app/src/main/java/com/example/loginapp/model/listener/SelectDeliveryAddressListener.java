package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface SelectDeliveryAddressListener {
    void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses);

    void isDeliveryAddressEmpty();
}
