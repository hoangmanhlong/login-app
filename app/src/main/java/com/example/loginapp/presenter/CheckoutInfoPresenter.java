package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.interator.CheckoutInfoInterator;
import com.example.loginapp.model.listener.CheckoutInfoListener;
import com.example.loginapp.view.fragment.checkout.CheckoutInfoView;

import java.util.List;

public class CheckoutInfoPresenter implements CheckoutInfoListener {
    private final CheckoutInfoInterator interator = new CheckoutInfoInterator(this);

    private final CheckoutInfoView view;

    private Order currentOrder;

    private DeliveryAddress selectedDeliveryAddress;

    public CheckoutInfoPresenter(CheckoutInfoView view) {
        this.view = view;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public DeliveryAddress getSelectedDeliveryaddress() {
        return selectedDeliveryAddress;
    }

    public void setSelectedDeliveryAddress(DeliveryAddress selectedDeliveryAddress) {
        this.selectedDeliveryAddress = selectedDeliveryAddress;
    }

    public DeliveryAddress checkInput(String name, String phoneNumber, String address, String province, String postalCode, String country, String shippingOption) {
        if (!name.isEmpty() && !address.isEmpty() && !province.isEmpty() && !postalCode.isEmpty() && !country.isEmpty() && !shippingOption.isEmpty()) {
            return new DeliveryAddress(name,phoneNumber, address, province, Integer.parseInt(postalCode), country, shippingOption);
        } else {
            return null;
        }
    }

    @Override
    public void onMessage(String message) {

    }
}
