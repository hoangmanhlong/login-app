package com.example.loginapp.adapter.select_address_adapter;

import com.example.loginapp.model.entity.DeliveryAddress;

public interface OnSelectDeliveryAddressClickListener {
    void onDeliveryAddressClick(DeliveryAddress deliveryAddress);

    void enableOkButton();
}
