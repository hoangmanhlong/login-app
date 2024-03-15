package com.example.loginapp.view.fragments.shipping_address;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressView {
    void bindShippingAddresses(List<DeliveryAddress> deliveryAddress);

    void isLoading(boolean loading);

    void isDeliveryAddressEmpty(boolean isEmpty);

    void addNewDeliveryAddressButtonVisible(boolean visible);
}
