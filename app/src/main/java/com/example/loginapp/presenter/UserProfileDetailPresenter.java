package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.UserProfileInteractor;
import com.example.loginapp.model.listener.UserProfileDetailListener;
import com.example.loginapp.view.fragments.user_profile.UserProfileDetailView;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDetailPresenter implements UserProfileDetailListener {

    private static final String TAG = UserProfileDetailPresenter.class.getSimpleName();

    private UserProfileDetailView view;

    private Boolean userDataIsObtainedForTheFirstTime = false;

    /**
     * @English List of orders taken for the first time
     * @Vietnamese Danh sách đơn hàng được lấy lần đầu tiên
     * @Default false
     */
    private Boolean listOfOrdersTakenForTheFirstTime = false;

    private UserProfileInteractor interactor;

    private AppSharedPreferences sharedPreferences;

    private UserData userData;

    private Boolean isVietnamese;

    public UserData getUserData() {
        return userData;
    }

    private List<Order> orders;

    public UserProfileDetailPresenter(UserProfileDetailView view) {
        this.view = view;
        orders = new ArrayList<>();
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
        isVietnamese = sharedPreferences.getLanguage();
        interactor = new UserProfileInteractor(this);
    }

    public void initData() {
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
        interactor.addOrdersValueEventListener();
    }

    public void removeOrdersValueEventListener() {
        interactor.removeOrdersValueEventListener();
    }

    @Override
    public void getUserData(UserData userData) {
        this.userData = userData;
        if (view != null) view.bindUserData(this.userData);
        userDataIsObtainedForTheFirstTime = true;
    }

    public void addUserDataValueEventListener() {
        interactor.addUserDataValueEventListener();
    }

    public void removeUserDataValueEventListener() {
        interactor.removeUserDataValueEventListener();
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

    public void clear() {
        view = null;
        sharedPreferences = null;
        isVietnamese = null;
        userData = null;
        orders = null;
        listOfOrdersTakenForTheFirstTime = null;
        userDataIsObtainedForTheFirstTime = null;
        interactor.clear();
        interactor = null;
    }
}
