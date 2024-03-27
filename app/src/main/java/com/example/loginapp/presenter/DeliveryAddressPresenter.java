package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressInteractor;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.example.loginapp.view.fragments.shipping_address.DeliveryAddressView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressPresenter implements DeliveryAddressListener {

    private DeliveryAddressView view;

    private DeliveryAddressInteractor interactor;

    private List<DeliveryAddress> deliveryAddresses;

    private boolean wasTakenForTheFirstTime = false;

    public DeliveryAddressPresenter(DeliveryAddressView view) {
        this.view = view;
        deliveryAddresses = new ArrayList<>();
        interactor = new DeliveryAddressInteractor(this);
    }

    public void clear() {
        view = null;
        interactor.clear();
        interactor = null;
        deliveryAddresses = null;
    }

    public void initData() {
        if (wasTakenForTheFirstTime) {
            view.isLoading(false);
            if (deliveryAddresses.isEmpty()) {
                view.isDeliveryAddressEmpty(true);
                view.addNewDeliveryAddressButtonVisible(true);
            }
            else {
                view.bindShippingAddresses(deliveryAddresses);
                view.addNewDeliveryAddressButtonVisible(deliveryAddresses.size() < 3);
            }
        }
    }

    public void addDeliveryAddressValueEventListener() {
        view.isLoading(true);
        interactor.addDeliveryAddressValueEventListener();
    }

    public void removeDeliveryAddressValueEventListener() {
        interactor.removeDeliveryAddressValueEventListener();
    }

    @Override
    public void getDeliveryAddress(List<DeliveryAddress> deliveryAddresses) {
        view.isLoading(false);
        view.isDeliveryAddressEmpty(false);
        this.deliveryAddresses = deliveryAddresses;
        view.addNewDeliveryAddressButtonVisible(deliveryAddresses.size() < 3);
        view.bindShippingAddresses(deliveryAddresses);
        wasTakenForTheFirstTime = true;
    }

    @Override
    public void isDeliveryAddressEmpty() {
        view.isLoading(false);
        view.isDeliveryAddressEmpty(true);
        view.addNewDeliveryAddressButtonVisible(true);
        deliveryAddresses.clear();
        wasTakenForTheFirstTime = true;
    }
}
