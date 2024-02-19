package com.example.loginapp.presenter;

import com.example.loginapp.data.local.AssertReader;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressDetailInteractor;
import com.example.loginapp.model.listener.DeliveryAddressDetailListener;
import com.example.loginapp.view.fragments.delivery_address_detail.DeliveryAddressDetailView;

public class DeliveryAddressDetailPresenter implements DeliveryAddressDetailListener {

    private final DeliveryAddressDetailInteractor interactor = new DeliveryAddressDetailInteractor(this);

    private final DeliveryAddressDetailView view;

    private DeliveryAddress currentDeliveryAddress;

    public DeliveryAddressDetailPresenter(DeliveryAddressDetailView view) {
        this.view = view;
    }

    public void setCurrentDeliveryAddress(DeliveryAddress deliveryAddress) {
        currentDeliveryAddress = deliveryAddress;
        view.bindAddress(currentDeliveryAddress);
    }

    public void getProvince() {
        view.bindProvinces(AssertReader.getProvinces());
    }

    public void deleteDeliveryAddress() {
        view.isLoading(true);
        interactor.deleteDeliveryAddress(currentDeliveryAddress.getDeliveryAddressId());
    }

    public void updateDeliveryAddress(String name, String phoneNumber, String address, String province, String postalCode, String country, String shippingOptions, Boolean isDefault) {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                currentDeliveryAddress != null ? currentDeliveryAddress.getDeliveryAddressId() : ("DA" + System.currentTimeMillis()),
                name,
                phoneNumber,
                address,
                province,
                Integer.parseInt(postalCode),
                country,
                shippingOptions.isEmpty() ? "" : shippingOptions,
                isDefault
        );
        interactor.updateDeliveryAddress(currentDeliveryAddress == null, deliveryAddress);
    }

    @Override
    public void isLoading(Boolean loading) {
        view.isLoading(loading);
    }

    @Override
    public void deleteSuccess() {
        view.isLoading(false);
        view.navigateUp();
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    @Override
    public void isUpdateSuccess(Boolean success) {
        if (success) {
            view.isLoading(false);
            view.navigateUp();
        }
    }
}
