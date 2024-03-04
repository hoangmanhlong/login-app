package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.view.fragments.buy_again.BuyAgainView;

public class BuyAgainPresenter {

    private final BuyAgainView view;

    private Order currentOrder;

    public BuyAgainPresenter(BuyAgainView view) {
        this.view = view;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
        view.bindAddress(currentOrder.getDeliveryAddress());
        view.bindOrderProducts(currentOrder.getOrderProducts());
    }
}
