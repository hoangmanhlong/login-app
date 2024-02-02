package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressDetailInteractor;
import com.example.loginapp.model.listener.DeliveryAddressDetailListener;
import com.example.loginapp.view.fragment.delivery_address_detail.DeliveryAddressDetailView;

public class DeliveryAddressDetailPresenter implements DeliveryAddressDetailListener {

    private final DeliveryAddressDetailInteractor interactor = new DeliveryAddressDetailInteractor(this);

    private final DeliveryAddressDetailView view;

    private DeliveryAddress currentDeliveryAddress;

    public DeliveryAddressDetailPresenter(DeliveryAddressDetailView view) {
        this.view = view;
    }

    public DeliveryAddress getCurrentDeliveryAddress() {
        return currentDeliveryAddress;
    }

    public void setCurrentDeliveryAddress(DeliveryAddress currentDeliveryAddress) {
        this.currentDeliveryAddress = currentDeliveryAddress;
    }

    public void deleteDeliveryAddress() {
        view.isLoading(true);
        interactor.deleteDeliveryAddress(currentDeliveryAddress.getDeliveryAddressId());
    }

    private String TAG = this.toString();

    public void updateDeliveryAddress(String name, String phoneNumber, String address, String province, String postalCode, String country, String shippingOptions, Boolean isDefault) {
        DeliveryAddress deliveryAddress;
        Boolean isNewDeliveryAddress = null;
        if (currentDeliveryAddress == null) {
            isNewDeliveryAddress = true;
            deliveryAddress = new DeliveryAddress(name, phoneNumber, address, province, Integer.parseInt(postalCode), country, shippingOptions.isEmpty() ? "" : shippingOptions, isDefault);
        } else {
            isNewDeliveryAddress = false;
            deliveryAddress = new DeliveryAddress(currentDeliveryAddress.getDeliveryAddressId(), name, phoneNumber, address, province, Integer.parseInt(postalCode), country, shippingOptions.isEmpty() ? "" : shippingOptions, isDefault);
        }
        interactor.updateDeliveryAddress(isNewDeliveryAddress, deliveryAddress);
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

    }

    @Override
    public void numberMax() {

    }

    @Override
    public void isUpdateSuccess(Boolean success) {
        if (success) {
            view.isLoading(false);
            view.navigateUp();
        }
    }
}
