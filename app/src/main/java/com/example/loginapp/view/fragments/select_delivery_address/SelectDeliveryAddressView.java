package com.example.loginapp.view.fragments.select_delivery_address;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface SelectDeliveryAddressView {
    void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses);

    void getDataShared();

}
