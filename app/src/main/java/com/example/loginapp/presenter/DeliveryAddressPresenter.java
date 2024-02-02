package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressInterator;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.example.loginapp.view.fragment.shipping_address.DeliveryAddressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeliveryAddressPresenter implements DeliveryAddressListener {

    private final DeliveryAddressView view;

    private final DeliveryAddressInterator interator = new DeliveryAddressInterator(this);

    private final List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    public DeliveryAddressPresenter(DeliveryAddressView view) {
        this.view = view;
    }

    public void initData() {
        if (deliveryAddresses.size() == 0) getDeliveryAddresses();
        else {
            view.getShippingAddresses(deliveryAddresses);
            isAddNewAddressButtonEnabled();
        }
    }

    public void getDeliveryAddresses() {
        view.isLoading(true);
        interator.getShippingAddresses();
    }

    private void isAddNewAddressButtonEnabled() {
        view.isAddNewAddressButtonEnabled(this.deliveryAddresses.size() < 3);
    }

    @Override
    public void getShippingAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddresses.add(deliveryAddress);
        isAddNewAddressButtonEnabled();
        view.getShippingAddresses(deliveryAddresses);
    }

    @Override
    public void notifyItemChanged(DeliveryAddress deliveryAddress) {
        int index = deliveryAddresses.indexOf(deliveryAddresses.stream().filter(p -> Objects.equals(p.getDeliveryAddressId(), deliveryAddress.getDeliveryAddressId())).collect(Collectors.toList()).get(0));
        deliveryAddresses.set(index, deliveryAddress);
        isAddNewAddressButtonEnabled();
        view.notifyItemChanged(index);
    }

    @Override
    public void notifyItemRemoved(DeliveryAddress deliveryAddress) {
        int index = deliveryAddresses.indexOf(deliveryAddresses.stream().filter(p -> Objects.equals(p.getDeliveryAddressId(), deliveryAddress.getDeliveryAddressId())).collect(Collectors.toList()).get(0));
        deliveryAddresses.remove(index);
        isAddNewAddressButtonEnabled();
        view.notifyItemRemoved(index);
    }
}
