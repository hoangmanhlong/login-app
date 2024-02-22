package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInterator;
import com.example.loginapp.model.listener.UserProfileListener;
import com.example.loginapp.view.fragments.user_profile.UserProfileView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserProfilePresenter implements UserProfileListener {

    private final UserProfileView view;

    private final UserProfileInterator interator = new UserProfileInterator(this);

    private UserData currentUser;

    public UserData getCurrentUser() {
        return currentUser;
    }

    private int numberOfProcessingOrder , numberOfCompletedOrder, numberOfCancelOrder, numberOfReturnOrder = -1;

    public UserProfilePresenter(UserProfileView view) {
        this.view = view;
    }

    private void getUserData() {
        interator.getUserData();
    }

    public void initData() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)  {
            view.isLogged(true);
            if (currentUser == null) getUserData();
            else view.bindUserData(currentUser);
            if (numberOfCancelOrder == -1 || numberOfCompletedOrder == -1 || numberOfProcessingOrder == -1 || numberOfReturnOrder == -1)
                getOrdersStatus();
            else
                view.bindNumberOfOrders(numberOfProcessingOrder, numberOfCompletedOrder, numberOfCancelOrder, numberOfReturnOrder);
        } else {
            view.isLogged(false);
        }

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
        numberOfProcessingOrder = countOrdersWithStatus(orders, OrderStatus.Processing);
        numberOfCompletedOrder = countOrdersWithStatus(orders, OrderStatus.Completed);
        numberOfCancelOrder = countOrdersWithStatus(orders, OrderStatus.Cancel);
        numberOfReturnOrder = countOrdersWithStatus(orders, OrderStatus.Return);
        view.bindNumberOfOrders(numberOfProcessingOrder, numberOfCompletedOrder, numberOfCancelOrder, numberOfReturnOrder);
    }

    private int countOrdersWithStatus(List<Order> orders, OrderStatus status) {
        return (int) orders.stream().filter(o -> o.getOrderStatus() == status).count();
    }

    @Override
    public void isEmptyOrdersList(boolean isEmpty) {
        if (isEmpty) {
            numberOfProcessingOrder = 0;
            numberOfCompletedOrder = 0;
            numberOfCancelOrder = 0;
            numberOfReturnOrder = 0;
        }
        view.bindNumberOfOrders(numberOfProcessingOrder, numberOfCompletedOrder, numberOfCancelOrder, numberOfReturnOrder);
    }
}
