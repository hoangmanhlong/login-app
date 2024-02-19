
package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.interator.OrderInterator;
import com.example.loginapp.model.listener.OrderListener;
import com.example.loginapp.view.fragments.orders.OrderView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CompletedOrdersPresenter implements OrderListener {

    private final OrderView view;

    private final OrderInterator interator = new OrderInterator(this);

    private List<Order> orders = new ArrayList<>();

    public CompletedOrdersPresenter(OrderView view) {
        this.view = view;
    }

    public void initData() {
        if (orders.isEmpty()) getOrders();
        else view.getOrders(orders);
    }

    public void getOrders() {
        view.isLoading(true);
        interator.getOrders();
    }

    @Override
    public void getOrders(List<Order> orders) {
        this.orders = orders.stream()
                .filter(p -> p.getOrderStatus() == OrderStatus.Completed)
                .sorted(Comparator.comparing(Order::getOrderId).reversed())
                .collect(Collectors.toList());
        view.isLoading(false);
        view.getOrders(this.orders);
    }
}
