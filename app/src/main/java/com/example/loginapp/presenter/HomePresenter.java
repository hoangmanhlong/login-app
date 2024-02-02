package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.HomeInterator;
import com.example.loginapp.model.listener.HomeListener;
import com.example.loginapp.view.fragment.home.HomeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HomePresenter implements HomeListener {

    private final String TAG = this.toString();

    private final HomeView view;

    private final HomeInterator interator = new HomeInterator(this);

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
        else {
            view.showRecommendedProducts(recommendedProducts);
            view.showTopChartsProducts(topChartsProducts);
            view.showDiscountProducts(discountProducts);
        }
        if (currentUserData == null) getUserData();
        else view.getUserData(currentUserData);
    }

    public void getUserData() {
        interator.getUserData();
    }

    public void getListProductFromNetwork() {
        interator.getListProductFromNetwork();
    }

    @Override
    public void getUserData(UserData userData) {
        view.getUserData(userData);
        currentUserData = userData;
    }

    @Override
    public void getApiProducts(List<Product> products) {
        this.products.addAll(products);
        getProducts(products);
    }

    private void getProducts(List<Product> products) {
        interator.getFavoriteProductFromFirebase();
        getTopChartsProducts(products);
        getDiscountProducts(products);
    }

    private void getTopChartsProducts(List<Product> products) {
        topChartsProducts = products.stream().filter(v -> v.getRating() > 4.8f).collect(Collectors.toList());
        view.showTopChartsProducts(topChartsProducts);
    }

    private void getDiscountProducts(List<Product> products) {
        discountProducts = products.stream().filter(v -> v.getDiscountPercentage() > 12.00).collect(Collectors.toList());
        view.showDiscountProducts(discountProducts);
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    @Override
    public void showProcessBar(Boolean show) {
        view.showProcessBar(show);
    }

    @Override
    public void getFavoriteProducts(List<Product> products) {
        List<String> categories = products.stream().map(Product::getCategory).distinct().collect(Collectors.toList());
        Log.d(TAG, "getFavoriteProducts: " + categories.size());
        int randomIndex = new Random().nextInt(categories.size());
        recommendedProducts = this.products.stream().filter(p -> p.getCategory().equals(categories.get(randomIndex))).collect(Collectors.toList());
        view.showRecommendedProducts(recommendedProducts);
        view.showProcessBar(false);
    }
}
