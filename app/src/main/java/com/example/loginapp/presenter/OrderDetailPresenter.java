package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.interator.OrderDetailInteractor;
import com.example.loginapp.model.listener.OrderDetailListener;
import com.example.loginapp.view.fragments.order_detail.OrderDetailView;

public class OrderDetailPresenter implements OrderDetailListener {

    private final OrderDetailInteractor interactor = new OrderDetailInteractor(this);

    private final OrderDetailView view;

    private Order order;

    public OrderDetailPresenter(OrderDetailView view) {
        this.view = view;
    }

    public void setOrder(Order order) {
        this.order = order;
        view.bindOrder(order);
    }

    public Order getOrder() {
        return order;
    }

    public void cancelOrder() {
        order.setOrderStatus(OrderStatus.Cancel);
        interactor.cancelOrder(order);
    }

    @Override
    public void isProcessSuccess(boolean isSuccess) {
        if (isSuccess) view.backPreviousScreen();
    }
}
