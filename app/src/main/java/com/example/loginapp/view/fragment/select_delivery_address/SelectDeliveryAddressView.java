package com.example.loginapp.view.fragment.select_delivery_address;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface SelectDeliveryAddressView {
    void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses);

}
