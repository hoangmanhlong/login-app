package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.interator.OrdersInterator;
import com.example.loginapp.model.listener.OrdersListener;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.orders.OrdersMessage;
import com.example.loginapp.view.fragments.orders.OrdersView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OrdersPresenter implements OrdersListener {

    private OrdersInterator interator;

    public void addOrdersValueEventListener() {
        interator.addOrdersValueEventListener();
    }

    public void removeOrdersValueEventListener() {
        interator.removeOrdersValueEventListener();
    }


    public OrdersPresenter() {
        interator = new OrdersInterator(this);
    }

    @Override
    public void getOrders(List<Order> orders) {
        EventBus.getDefault().postSticky(new OrdersMessage(orders));
    }

    @Override
    public void isOrdersEmpty() {
        EventBus.getDefault().postSticky(new OrdersMessage(new ArrayList<>()));
    }

    public void clear() {
        interator.clear();
        interator = null;
    }
}
