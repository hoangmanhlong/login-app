package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressInterator;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.example.loginapp.view.fragments.shipping_address.DeliveryAddressView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressPresenter implements DeliveryAddressListener {

    private DeliveryAddressView view;

    private DeliveryAddressInterator interator;

    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    private boolean wasTakenForTheFirstTime = false;

    public DeliveryAddressPresenter(DeliveryAddressView view) {
        this.view = view;
        interator = new DeliveryAddressInterator(this);
    }

    public void clear() {
        view = null;
        interator.clear();
        interator = null;
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
        interator.addDeliveryAddressValueEventListener();
    }

    public void removeDeliveryAddressValueEventListener() {
        interator.removeDeliveryAddressValueEventListener();
    }

    @Override
    public void getDeliveryAddress(List<DeliveryAddress> deliveryAddresses) {
        view.isLoading(false);
        this.deliveryAddresses = deliveryAddresses;
        view.isDeliveryAddressEmpty(false);
        view.bindShippingAddresses(deliveryAddresses);
        view.addNewDeliveryAddressButtonVisible(deliveryAddresses.size() < 3);
        wasTakenForTheFirstTime = true;
    }

    @Override
    public void isDeliveryAddressEmpty() {
        view.isLoading(false);
        deliveryAddresses.clear();
        view.isDeliveryAddressEmpty(true);
        view.addNewDeliveryAddressButtonVisible(true);
        wasTakenForTheFirstTime = true;
    }
}
