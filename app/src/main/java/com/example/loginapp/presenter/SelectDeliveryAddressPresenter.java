package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.SelectDeliveryAddressInteractor;
import com.example.loginapp.model.listener.SelectDeliveryAddressListener;
import com.example.loginapp.view.fragments.select_delivery_address.SelectDeliveryAddressView;

import java.util.ArrayList;
import java.util.List;

public class SelectDeliveryAddressPresenter implements SelectDeliveryAddressListener {

    private SelectDeliveryAddressInteractor interactor;

    private SelectDeliveryAddressView view;

    private DeliveryAddress selectedAddress;

    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    public SelectDeliveryAddressPresenter(SelectDeliveryAddressView view) {
        this.view = view;
        interactor = new SelectDeliveryAddressInteractor(this);
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

    public void clear() {
        view = null;
        selectedAddress = null;
        deliveryAddresses = null;
        interactor.clear();
        interactor = null;
    }

    public void getDeliveryAddresses() {
        interactor.getDeliveryAddresses();
    }

    @Override
    public void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
        view.isDeliveryAddressEmpty(false);
        view.getDeliveryAddresses(deliveryAddresses);
    }

    @Override
    public void isDeliveryAddressEmpty() {
        view.isDeliveryAddressEmpty(true);
    }
}
