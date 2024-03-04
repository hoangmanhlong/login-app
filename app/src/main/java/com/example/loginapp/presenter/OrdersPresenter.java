package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.interator.OrdersInterator;
import com.example.loginapp.model.listener.OrdersListener;
import com.example.loginapp.view.fragments.orders.OrdersView;

import java.util.ArrayList;
import java.util.List;

public class OrdersPresenter implements OrdersListener {

    private final OrdersInterator interator = new OrdersInterator(this);

    private final OrdersView view;

//    private List<Order> orders = new ArrayList<>();


    public OrdersPresenter(OrdersView view) {
        this.view = view;
    }

    public void initData() {
        getOrders();
//        else view.getOrders(orders);
    }

    private void getOrders() {
        interator.getOrders();
    }

    @Override
    public void getOrders(List<Order> order) {
//        this.orders = order;
        view.getOrders(order);
    }

    @Override
    public void isOrdersEmpty() {
        view.getOrders(new ArrayList<>());
    }
}
