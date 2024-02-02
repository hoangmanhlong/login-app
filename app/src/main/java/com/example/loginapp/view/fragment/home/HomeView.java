package com.example.loginapp.view.fragment.home;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;

import java.util.List;

public interface HomeView {
    void getUserData(UserData userData);

    void onMessage(String message);

    void showProcessBar(Boolean show);

    void showRecommendedProducts(List<Product> products);

    void showTopChartsProducts(List<Product> products);

    void showDiscountProducts(List<Product> products);

}
