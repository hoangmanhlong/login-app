package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.SelectDeliveryAddressInteractor;
import com.example.loginapp.model.listener.SelectDeliveryAddressListener;
import com.example.loginapp.view.fragments.select_delivery_address.SelectDeliveryAddressView;

import java.util.ArrayList;
import java.util.List;

public class SelectDeliveryAddressPresenter implements SelectDeliveryAddressListener {

    private final SelectDeliveryAddressInteractor interactor = new SelectDeliveryAddressInteractor(this);

    private final SelectDeliveryAddressView view;

    private DeliveryAddress selectedAddress;

    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    public SelectDeliveryAddressPresenter(SelectDeliveryAddressView view) {
        this.view = view;
    }

    public DeliveryAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(DeliveryAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public void setDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
        view.getDeliveryAddresses(deliveryAddresses);
    }

    public void initData() {
        getDeliveryAddresses();
//        if (deliveryAddresses.isEmpty()) view.getDataShared();
//        else view.getDeliveryAddresses(deliveryAddresses);
    }

    public void getDeliveryAddresses() {
        interactor.getDeliveryAddresses();
    }

    @Override
    public void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
        view.getDeliveryAddresses(deliveryAddresses);
    }
}
