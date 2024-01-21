package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.HomeInterator;
import com.example.loginapp.model.listener.HomeListener;
import com.example.loginapp.view.fragment.home.HomeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomePresenter implements HomeListener {

    private final HomeView view;

    private final HomeInterator interator = new HomeInterator(this);
    ;

    private final List<Product> products = new ArrayList<>();

    public List<Product> recommendedProducts = new ArrayList<>();

    public List<Product> topChartsProducts = new ArrayList<>();

    public List<Product> discountProducts = new ArrayList<>();

    public UserData currentUserData;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void iniData() {
        if (products.size() == 0) getListProductFromNetwork();
        if (currentUserData == null) getUserData();
        else view.getUserData(currentUserData);
    }

    public void getUserData() {
        interator.getUserData();
    }

    public void getListProductFromNetwork() {
        interator.getListProductFromNetwork();
    }

//    public void getProductOfCategory(String category) {
//        interator.getProductOfCategory(category);
//    }

    @Override
    public void getUserData(UserData userData) {
        currentUserData = userData;
        view.getUserData(userData);
    }

    @Override
    public void onLoadProducts(List<Product> products) {
        this.products.addAll(products);
        recommendedProducts = products.subList(0, Math.min(products.size(), 20));
        Collections.shuffle(recommendedProducts);
        view.showRecommendedProducts(recommendedProducts);

        topChartsProducts = products.stream().filter(v -> v.getRating() > 4.8f).collect(Collectors.toList());
        view.showTopChartsProducts(topChartsProducts);

        discountProducts = products.stream().filter(v -> v.getDiscountPercentage() > 12.00).collect(Collectors.toList());
        view.showDiscountProducts(discountProducts);
        view.showProcessBar(false);
    }

    @Override
    public void onLoadError(String message) {
        view.onLoadError(message);
    }

    @Override
    public void onLoadCategories(List<String> categories) {
        view.onLoadCategories(categories);
    }

    @Override
    public void showProcessBar(Boolean show) {
        view.showProcessBar(show);
    }

}
