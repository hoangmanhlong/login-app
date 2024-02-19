package com.example.loginapp.view.fragments.shipping_address;

import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressView {
    void getShippingAddresses(List<DeliveryAddress> deliveryAddress);

    void isLoading(boolean loading);

    void isAddNewAddressButtonEnabled(Boolean enable);


    void notifyItemChanged(int index);

    void notifyItemRemoved(int index);

    void isListEmpty(Boolean isEmpty);
}
