package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInterator;
import com.example.loginapp.model.listener.UserProfileListener;
import com.example.loginapp.view.fragments.user_profile.UserProfileView;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePresenter implements UserProfileListener {

    private final UserProfileView view;

    private final UserProfileInterator interator = new UserProfileInterator(this);

    private UserData currentUser;

    public UserData getCurrentUser() {
        return currentUser;
    }

    private final List<Order> orders = new ArrayList<>();

    public UserProfilePresenter(UserProfileView view) {
        this.view = view;
    }

    private boolean isChecked = false;

    private void getUserData() {
        interator.getUserData();
    }

    public void initData() {
        if (currentUser == null) getUserData();
        else view.bindUserData(currentUser);

        if (isChecked) {
            if (orders.isEmpty()) view.bindNumberOfOrders(0, 0, 0, 0);
            else getOrders(orders);
        } else getOrdersStatus();
    }

    private void getOrdersStatus() {
        interator.getOrderStatus();
    }

    @Override
    public void getUserData(UserData userData) {
        currentUser = userData;
        view.bindUserData(currentUser);
    }

    @Override
    public void getOrders(List<Order> orders) {
        isChecked = true;
        int numberOfProcessingOrder = countOrdersWithStatus(orders, OrderStatus.Processing);
        int numberOfCompletedOrder = countOrdersWithStatus(orders, OrderStatus.Completed);
        int numberOfCancelOrder = countOrdersWithStatus(orders, OrderStatus.Cancel);
        int numberOfReturnOrder = countOrdersWithStatus(orders, OrderStatus.Return);
        view.bindNumberOfOrders(numberOfProcessingOrder, numberOfCompletedOrder, numberOfCancelOrder, numberOfReturnOrder);
    }

    @Override
    public void isOrdersListEmpty() {
        view.bindNumberOfOrders(0, 0, 0, 0);
    }

    private int countOrdersWithStatus(List<Order> orders, OrderStatus status) {
        return (int) orders.stream().filter(order -> order.getOrderStatus() == status).count();
    }
}
