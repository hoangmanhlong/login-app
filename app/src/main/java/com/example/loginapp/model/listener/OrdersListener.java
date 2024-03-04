package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Order;

import java.util.List;

public interface OrdersListener {
    void getOrders(List<Order> order);

    void isOrdersEmpty();
}
