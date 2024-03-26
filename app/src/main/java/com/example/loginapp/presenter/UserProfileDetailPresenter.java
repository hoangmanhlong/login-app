package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInterator;
import com.example.loginapp.model.listener.UserProfileDetailListener;
import com.example.loginapp.view.fragments.user_profile.UserProfileDetailView;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDetailPresenter implements UserProfileDetailListener {

    private final String TAG = this.toString();

    private UserProfileDetailView view;

    private boolean userDataIsObtainedForTheFirstTime = false;

    /**
     * @English List of orders taken for the first time
     * @Vietnamese Danh sách đơn hàng được lấy lần đầu tiên
     * @Default false
     */
    private boolean listOfOrdersTakenForTheFirstTime = false;

    private UserProfileInterator interator;

    private final AppSharedPreferences sharedPreferences;

    private UserData userData;

    private boolean isVietnamese;

    public UserData getUserData() {
        return userData;
    }

    private List<Order> orders = new ArrayList<>();

    public UserProfileDetailPresenter(UserProfileDetailView view) {
        this.view = view;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
        interator = new UserProfileInterator(this);
    }

    public boolean isVietnamese() {
        return isVietnamese;
    }

    public void initData() {
        isVietnamese = sharedPreferences.getLanguage();
        if (view != null) {
            view.bindLanguageState(isVietnamese);
            if (userDataIsObtainedForTheFirstTime && userData != null) view.bindUserData(userData);
            if (listOfOrdersTakenForTheFirstTime) {
                if (orders.isEmpty()) view.bindNumberOfOrders(0, 0, 0, 0);
                else getOrders(orders);
            }
        }
    }

    public void addOrdersValueEventListener() {
        interator.addOrdersValueEventListener();
    }

    public void removeOrdersValueEventListener() {
        interator.removeOrdersValueEventListener();
    }

    @Override
    public void getUserData(UserData userData) {
        this.userData = userData;
        if (view != null) view.bindUserData(this.userData);
        userDataIsObtainedForTheFirstTime = true;
    }

    public void addUserDataValueEventListener() {
        interator.addUserDataValueEventListener();
    }

    public void removeUserDataValueEventListener() {
        interator.removeUserDataValueEventListener();
    }

    @Override
    public void getOrders(List<Order> orders) {
        this.orders = orders;
        int numberOfProcessingOrder = countOrdersWithStatus(orders, OrderStatus.Processing);
        int numberOfCompletedOrder = countOrdersWithStatus(orders, OrderStatus.Completed);
        int numberOfCancelOrder = countOrdersWithStatus(orders, OrderStatus.Cancel);
        int numberOfReturnOrder = countOrdersWithStatus(orders, OrderStatus.Return);
        if (view != null)
            view.bindNumberOfOrders(numberOfProcessingOrder, numberOfCompletedOrder, numberOfCancelOrder, numberOfReturnOrder);
        listOfOrdersTakenForTheFirstTime = true;
    }

    @Override
    public void isOrdersListEmpty() {
        this.orders.clear();
        if (view != null) view.bindNumberOfOrders(0, 0, 0, 0);
        listOfOrdersTakenForTheFirstTime = true;
    }

    @Override
    public void userDataEmpty() {
        userDataIsObtainedForTheFirstTime = true;
    }

    private int countOrdersWithStatus(List<Order> orders, OrderStatus status) {
        return (int) orders.stream().filter(order -> order.getOrderStatus() == status).count();
    }

    public void detachView() {
        view = null;
        interator.clear();
        interator = null;
    }
}
