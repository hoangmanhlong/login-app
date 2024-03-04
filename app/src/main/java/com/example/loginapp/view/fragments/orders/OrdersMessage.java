package com.example.loginapp.view.fragments.orders;

import com.example.loginapp.model.entity.Order;

import java.util.List;

public class OrdersMessage {

    public final List<Order> orders;

    public OrdersMessage(List<Order> orders) {
        this.orders = orders;
    }
}
