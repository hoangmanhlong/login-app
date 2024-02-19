package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressListener {
    void notifyItemAdded(DeliveryAddress deliveryAddress);

    void isDeliveryAddressesEmpty(Boolean isEmpty);

    void notifyItemChanged(DeliveryAddress deliveryAddress);

    void notifyItemRemoved(DeliveryAddress deliveryAddress);
}
