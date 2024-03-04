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

    public void processOrder() {
        Order newOrder = order;
        if (order.getOrderStatus() == OrderStatus.Processing || order.getOrderStatus() == OrderStatus.Completed) {
            OrderStatus newOrderStatus = order.getOrderStatus() == OrderStatus.Processing ? OrderStatus.Cancel : OrderStatus.Return;
            newOrder.setOrderStatus(newOrderStatus);
            interactor.updateOrder(newOrder);
        } else {
            newOrder.setOrderStatus(OrderStatus.Processing);
            newOrder.setOrderId("SA" + System.currentTimeMillis());
            newOrder.setOrderDate(System.currentTimeMillis());
            interactor.buyAgain(newOrder);
        }
    }

    @Override
    public void isProcessSuccess(boolean isSuccess) {
        if (isSuccess) view.backPreviousScreen();
    }
}
