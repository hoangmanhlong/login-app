package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.interator.OrderDetailInteractor;
import com.example.loginapp.model.listener.OrderDetailListener;
import com.example.loginapp.view.fragments.order_detail.OrderDetailView;

public class OrderDetailPresenter implements OrderDetailListener {

    private static final String TAG = OrderDetailPresenter.class.getSimpleName();

    private OrderDetailInteractor interactor;

    private OrderDetailView view;

    private Order order;

    public OrderDetailPresenter(OrderDetailView view) {
        this.view = view;
        interactor = new OrderDetailInteractor(this);
    }

    public void clear() {
        view = null;
        interactor.clear();
        interactor = null;
        order = null;
    }

    public void initData() {
        if (order == null) view.getSharedOrder();
        else view.bindOrder(order);
    }

    public void setOrder(Order order) {
        this.order = new Order().copy(order);
        view.bindOrder(order);
    }

    public Order getOrder() {
        return order;
    }

    public void cancelOrReturnOrder() {
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == OrderStatus.Completed) order.setOrderStatus(OrderStatus.Return);
        if (orderStatus == OrderStatus.Processing) order.setOrderStatus(OrderStatus.Cancel);
        interactor.cancelOrder(order);
    }

    @Override
    public void isProcessSuccess(boolean isSuccess) {
        if (isSuccess) view.backPreviousScreen();
    }
}
