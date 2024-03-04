package com.example.loginapp.view.fragments.order_detail;

import com.example.loginapp.model.entity.Order;

public interface OrderDetailView {

    void bindOrder(Order order);

    void backPreviousScreen();
}
