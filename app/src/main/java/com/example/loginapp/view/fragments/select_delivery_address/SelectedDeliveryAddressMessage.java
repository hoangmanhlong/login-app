package com.example.loginapp.view.fragments.select_delivery_address;

import com.example.loginapp.model.entity.DeliveryAddress;

public class SelectedDeliveryAddressMessage {

    private final DeliveryAddress deliveryAddress;

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public SelectedDeliveryAddressMessage(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
