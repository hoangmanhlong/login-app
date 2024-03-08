package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.HomeInterator;
import com.example.loginapp.model.listener.HomeListener;
import com.example.loginapp.view.fragments.home.HomeView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomePresenter implements HomeListener {

    private final String TAG = HomePresenter.class.getName();

    private final HomeView view;

    private final HomeInterator interator = new HomeInterator(this);

    private final List<Product> products = new ArrayList<>();

    public List<Product> recommendedProducts = new ArrayList<>();

    public List<Product> topChartsProducts = new ArrayList<>();

    public List<Product> discountProducts = new ArrayList<>();

    private List<Product> bestSellerProducts = new ArrayList<>();

    public UserData currentUserData;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void initData() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)  {
            if (currentUserData == null) getUserData();
            else checkUser(currentUserData);
        } else {
            view.isUserLoading(false);
            view.setShowUserView(false);
        }

        if (products.isEmpty()) getListProductFromNetwork();
        else showProducts();

        if (bestSellerProducts.isEmpty()) getBestsellerProducts();
        else view.getBestsellerProducts(bestSellerProducts);
    }

    private void getBestsellerProducts() {
        interator.getBestsellerProducts();
    }

    private void getUserData() {
        view.isUserLoading(true);
        interator.getUserData();
    }

    public void getListProductFromNetwork() {
        view.isRecommendedProductsLoading(true);
        view.isTopChartsProductsLoading(true);
        view.isDiscountProductsLoading(true);
        interator.getListProductFromNetwork();
    }

    private void showProducts() {
        view.showRecommendedProducts(recommendedProducts);
        view.showTopChartsProducts(topChartsProducts);
        view.showDiscountProducts(discountProducts);
    }

    @Override
    public void getUserData(UserData userData) {
        currentUserData = userData;
        checkUser(userData);
    }

    private void checkUser(UserData userData) {
        view.isUserLoading(false);
        Boolean check = !userData.getUsername().isEmpty() && !userData.getPhotoUrl().isEmpty();
        view.setShowUserView(check);
        if (check) view.getUserData(userData);
    }

    @Override
    public void getProductsFromAPI(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        processProducts(products);
    }

    private void processProducts(List<Product> products) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            interator.getFavoriteProductFromFirebase();
        else {
            List<Product> tempProducts = new ArrayList<>(this.products);
            Collections.shuffle(tempProducts);
            recommendedProducts = tempProducts.subList(0, Math.min(tempProducts.size(), 20));
            view.isRecommendedProductsLoading(false);
            view.showRecommendedProducts(recommendedProducts);
        }
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
    public void getFavoriteProducts(List<Product> products) {
        if (!products.isEmpty()) {
            List<String> categories = products.stream().map(Product::getCategory).distinct().collect(Collectors.toList());
            recommendedProducts = this.products.stream().filter(p -> categories.contains(p.getCategory())).collect(Collectors.toList());
        } else {
            List<Product> tempProducts = new ArrayList<>(this.products);
            Collections.shuffle(tempProducts);
            recommendedProducts = tempProducts.subList(0, Math.min(tempProducts.size(), 20));
        }
        view.showRecommendedProducts(recommendedProducts);
        view.refreshInvisible();
    }

    @Override
    public void getBestsellerProducts(List<Product> products) {
        bestSellerProducts = products;
        view.getBestsellerProducts(products);
    }
}
