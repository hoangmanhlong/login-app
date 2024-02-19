package com.example.loginapp.view.fragments.delivery_address_detail;

import com.example.loginapp.model.entity.DeliveryAddress;

public interface DeliveryAddressDetailView {

    void isLoading(Boolean loading);

    void navigateUp();

    void onMessage(String message);

    void bindAddress(DeliveryAddress deliveryAddress);

    void bindProvinces(String[] provinces);
}
