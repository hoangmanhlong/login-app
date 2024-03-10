package com.example.loginapp.presenter;

import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

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

    private final UserProfileDetailView view;

    private final UserProfileInterator interator = new UserProfileInterator(this);

    private final AppSharedPreferences sharedPreferences;

    private UserData currentUser;

    private boolean isVietnamese;

    public UserData getCurrentUser() {
        return currentUser;
    }

    private final List<Order> orders = new ArrayList<>();

    public UserProfileDetailPresenter(UserProfileDetailView view) {
        this.view = view;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
    }

    private boolean isChecked = false;

    private void getUserData() {
        interator.getUserData();
    }


    public void changeLanguage() {
        sharedPreferences.setLanguage(!isVietnamese);
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(isVietnamese ? "en-US" : "vi");
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    public void initData() {
        isVietnamese = sharedPreferences.getLanguage();
        view.bindLanguageState(isVietnamese);
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
