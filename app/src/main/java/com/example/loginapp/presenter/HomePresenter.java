package com.example.loginapp.presenter;

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

    private  static final String TAG = HomePresenter.class.getName();

    private final HomeView view;

    private final HomeInterator interator = new HomeInterator(this);

    private List<Product> products = new ArrayList<>();

    public List<Product> recommendedProducts = new ArrayList<>();

    public List<Product> topChartsProducts = new ArrayList<>();

    public List<Product> discountProducts = new ArrayList<>();

    private List<Product> recommendedEveryDay = new ArrayList<>();

    public UserData currentUserData;

    private final boolean authenticated;

    private boolean gotDataFromAPI = false;

    public HomePresenter(HomeView view) {
        this.view = view;
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void initData() {
        view.showAskLogin(!authenticated);
        if (authenticated) {
            if (currentUserData == null) getUserData();
            else getUserData(currentUserData);
        } else {
            view.isUserLoading(false);
            view.setShowUserView(false);
        }

        if (gotDataFromAPI) showProducts();
        else getListProductFromNetwork();
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
        view.bindRecommendedEveryDay(recommendedEveryDay);
        view.showRecommendedProducts(recommendedProducts);
        view.showTopChartsProducts(topChartsProducts);
        view.showDiscountProducts(discountProducts);
    }

    @Override
    public void getUserData(UserData userData) {
        currentUserData = userData;
        view.isUserLoading(false);
        boolean isUserViewVisible = currentUserData.getAvatar() != null || currentUserData.getUsername() != null;
        if (isUserViewVisible) view.getUserData(userData);
        view.setShowUserView(isUserViewVisible);
    }

    @Override
    public void getProductsFromAPI(List<Product> products) {
        this.products = products;
        gotDataFromAPI = true;
        productClassification(products);
    }

    private void productClassification(List<Product> products) {
        getRecommendedEveryDay(products);
        getTopChartsProducts(products);
        getDiscountProducts(products);
        if (authenticated) interator.getFavoriteProductFromFirebase();
        else {
            List<Product> tempProducts = new ArrayList<>(this.products);
            Collections.shuffle(tempProducts);
            recommendedProducts = tempProducts.subList(0, Math.min(tempProducts.size(), 30));
            view.showRecommendedProducts(recommendedProducts);
            view.refreshInvisible();
        }
    }

    private void getRecommendedEveryDay(List<Product> products) {
        Collections.shuffle(products);
        recommendedEveryDay = products.subList(0, Math.min(products.size(), 10));
        view.bindRecommendedEveryDay(recommendedEveryDay);
    }

    private void getTopChartsProducts(List<Product> products) {
        topChartsProducts = products.stream().filter(product -> product.getRating() > 4.8f).collect(Collectors.toList());
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
    public void isUserDataEmpty() {
        view.isUserLoading(false);
        view.setShowUserView(false);
    }
}
