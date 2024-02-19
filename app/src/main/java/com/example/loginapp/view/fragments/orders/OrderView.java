package com.example.loginapp.view.fragments.orders;

import com.example.loginapp.model.entity.Order;

import java.util.List;

public interface OrderView {
    void getOrders(List<Order> orders);

    void isLoading(Boolean loading);
}
