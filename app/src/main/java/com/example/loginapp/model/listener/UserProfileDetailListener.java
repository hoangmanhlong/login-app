package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.UserData;

import java.util.List;

public interface UserProfileDetailListener {
    void getUserData(UserData userData);

    void getOrders(List<Order> orders);

    void isOrdersListEmpty();
}
