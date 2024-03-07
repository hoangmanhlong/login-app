package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.interator.CheckoutInfoInterator;
import com.example.loginapp.model.listener.CheckoutInfoListener;
import com.example.loginapp.view.fragments.checkout.CheckoutInfoView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutInfoPresenter implements CheckoutInfoListener {

    private final CheckoutInfoInterator interator = new CheckoutInfoInterator(this);

    private final CheckoutInfoView view;

    private Order currentOrder;

    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();


    public CheckoutInfoPresenter(CheckoutInfoView view) {
        this.view = view;
    }

    public void getDefaultDeliveryAddress() {
        view.isLoading(true);
        interator.getDeliveryAddresses();
    }

    public List<DeliveryAddress> getDeliveryAddresses() {
        return deliveryAddresses;
    }



    public void initData() {
        if (currentOrder == null) view.getSharedData();
        else view.bindDefaultDeliveryAddress(currentOrder.getDeliveryAddress());

        if (deliveryAddresses.isEmpty()) getDefaultDeliveryAddress();
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        currentOrder.setDeliveryAddress(deliveryAddress);
        view.bindDefaultDeliveryAddress(deliveryAddress);
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public DeliveryAddress checkInput(String name, String phoneNumber, String address, String province, String postalCode, String country, String shippingOption) {
        if (!name.isEmpty() && !address.isEmpty() && !province.isEmpty() && !postalCode.isEmpty() && !country.isEmpty()) {
            return new DeliveryAddress(name, phoneNumber, address, province, Integer.parseInt(postalCode), country, shippingOption.isEmpty() ? "" : shippingOption);
        } else {
            return null;
        }
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        view.isLoading(false);
        this.deliveryAddresses = deliveryAddresses;
        DeliveryAddress defaultDeliveryAddress = deliveryAddresses.stream().filter(DeliveryAddress::getIsDefault).collect(Collectors.toList()).get(0);
        currentOrder.setDeliveryAddress(defaultDeliveryAddress);
        view.bindDefaultDeliveryAddress(defaultDeliveryAddress);

    }

    @Override
    public void isDeliveryAddressesEmpty() {
        view.isLoading(false);
    }
}
