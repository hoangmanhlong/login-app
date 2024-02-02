package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.DeliveryAddress;

public interface DeliveryAddressListener {
    void getShippingAddress(DeliveryAddress deliveryAddress);

    void notifyItemChanged(DeliveryAddress deliveryAddress);

    void notifyItemRemoved(DeliveryAddress deliveryAddress);
}
