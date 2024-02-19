package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressInterator;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.example.loginapp.view.fragments.shipping_address.DeliveryAddressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeliveryAddressPresenter implements DeliveryAddressListener {

    private final DeliveryAddressView view;

    private final DeliveryAddressInterator interator = new DeliveryAddressInterator(this);

    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    public DeliveryAddressPresenter(DeliveryAddressView view) {
        this.view = view;
    }

    public void getDeliveryAddresses() {
        interator.getShippingAddresses();
    }

    public void initData() {
        if (deliveryAddresses.isEmpty()) getDeliveryAddresses();
        else {
            view.getShippingAddresses(deliveryAddresses);
            buttonState();
        }
    }

    @Override
    public void notifyItemAdded(DeliveryAddress deliveryAddress) {
        deliveryAddresses.add(deliveryAddress);
        view.getShippingAddresses(deliveryAddresses);
        buttonState();
    }

    @Override
    public void isDeliveryAddressesEmpty(Boolean isEmpty) {
        view.isListEmpty(isEmpty);
    }

    @Override
    public void notifyItemChanged(DeliveryAddress deliveryAddress) {
        int index = deliveryAddresses.indexOf(deliveryAddresses.stream().filter(p -> Objects.equals(p.getDeliveryAddressId(), deliveryAddress.getDeliveryAddressId())).collect(Collectors.toList()).get(0));
        deliveryAddresses.set(index, deliveryAddress);
        view.notifyItemChanged(index);
        buttonState();
    }

    private void buttonState() {
        view.isAddNewAddressButtonEnabled(deliveryAddresses.size() < 3);
    }

    @Override
    public void notifyItemRemoved(DeliveryAddress deliveryAddress) {
        int index = deliveryAddresses.indexOf(deliveryAddresses.stream().filter(p -> Objects.equals(p.getDeliveryAddressId(), deliveryAddress.getDeliveryAddressId())).collect(Collectors.toList()).get(0));
        deliveryAddresses.remove(index);
        view.notifyItemRemoved(index);
        buttonState();
    }
}
