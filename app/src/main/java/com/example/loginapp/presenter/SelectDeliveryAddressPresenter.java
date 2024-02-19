package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.SelectDeliveryAddressInteractor;
import com.example.loginapp.model.listener.SelectDeliveryAddressListener;
import com.example.loginapp.view.fragments.select_delivery_address.SelectDeliveryAddressView;

import java.util.List;

public class SelectDeliveryAddressPresenter implements SelectDeliveryAddressListener {

    private final SelectDeliveryAddressInteractor interactor = new SelectDeliveryAddressInteractor(this);

    private final SelectDeliveryAddressView view;

    public DeliveryAddress selectedAddress;

    public SelectDeliveryAddressPresenter(SelectDeliveryAddressView view) {
        this.view = view;
    }

    public void getDeliveryAddress() {
        interactor.getDeliveryAddresses();
    }

    @Override
    public void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        view.getDeliveryAddresses(deliveryAddresses);
    }
}
