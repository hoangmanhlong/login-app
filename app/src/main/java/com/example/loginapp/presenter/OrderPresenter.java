package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.OrderInterator;
import com.example.loginapp.model.listener.OrderListener;
import com.example.loginapp.view.fragment.orders.OrderView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPresenter implements OrderListener {

    private final OrderView view;

    private final OrderInterator interator = new OrderInterator(this);

    public List<Order> orders = new ArrayList<>();

    public OrderPresenter(OrderView view) {
        this.view = view;
    }

    public void initData() {
        if (orders.size() == 0) getOrders();
        else view.getOrders(orders);
    }

    public void getOrders() {
//        view.isLoading(true);
        interator.getOrders();
    }

    @Override
    public void getOrders(List<Order> orders) {
        this.orders = orders.stream().sorted(Comparator.comparing(Order::getOrderId).reversed())
                .collect(Collectors.toList());
        view.getOrders(this.orders);
    }
}
