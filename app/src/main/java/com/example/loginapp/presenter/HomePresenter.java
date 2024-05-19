package com.example.loginapp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.loginapp.App;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.interator.HomeInteractor;
import com.example.loginapp.model.listener.HomeListener;
import com.example.loginapp.utils.NetworkChecker;
import com.example.loginapp.view.fragments.home.HomeView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HomePresenter implements HomeListener {

    private static final String TAG = HomePresenter.class.getName();

    private HomeView view;

    private HomeInteractor interactor;

    private List<Product> products = new ArrayList<>(); // All Products get from API

    public List<Product> recommendedProducts = new ArrayList<>();

    public List<Product> topChartsProducts = new ArrayList<>();

    public List<Product> discountProducts = new ArrayList<>();

    private List<Product> recommendedEveryDay = new ArrayList<>();

    public UserData currentUserData;

    private Boolean authenticated;

    private Boolean gotDataFromAPI = false;

    private Boolean gotUserData = false;

    private ExecutorService executorService;

    private Handler handler;

    public void clear() {
        executorService.shutdown();
        handler.removeCallbacksAndMessages(null);
        executorService = null;
        handler = null;
        authenticated = null;
        gotDataFromAPI = null;
        gotUserData = null;
        view = null;
        interactor.clear();
        interactor = null;
        products = null;
        recommendedEveryDay = null;
        topChartsProducts = null;
        discountProducts = null;
        recommendedProducts = null;
        currentUserData = null;
    }

    public HomePresenter(HomeView view) {
        this.view = view;
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
        interactor = new HomeInteractor(this);
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void initData() {
        if (gotUserData && view != null) {
            view.isUserLoading(false);
            if (currentUserData != null) {
                view.setShowUserView(true);
                view.getUserData(currentUserData);
            } else {
                view.setShowUserView(false);
            }
        }

        if (gotDataFromAPI) showProducts();
        else getListProductFromNetwork();
    }

    public void addUserDataValueEventListener() {
        if (authenticated) {
            view.isUserLoading(!gotUserData);
            interactor.addUserDataValueEventListener();
        } else {
            view.isUserLoading(false);
            view.setShowUserView(false);
        }
    }

    public void removeUserDataValueEventListener() {
        if (authenticated) interactor.removeUserDataValueEventListener();
    }

    public void getListProductFromNetwork() {
        if (view != null && NetworkChecker.isNetworkAvailable(App.getInstance())) {
            interactor.getListProductFromNetwork();
            if (!gotDataFromAPI) {
                view.isRecommendedProductsLoading(true);
                view.isTopChartsProductsLoading(true);
                view.isDiscountProductsLoading(true);
            }
        }
    }

    private void showProducts() {
        if (view != null) {
            view.isRecommendedProductsLoading(false);
            view.isTopChartsProductsLoading(false);
            view.isDiscountProductsLoading(false);
            view.bindRecommendedEveryDay(recommendedEveryDay);
            view.showRecommendedProducts(recommendedProducts);
            view.showTopChartsProducts(topChartsProducts);
            view.showDiscountProducts(discountProducts);
        }
    }

    @Override
    public void getUserData(UserData userData) {
        gotUserData = true;
        currentUserData = userData;
        boolean isUserViewVisible =
                (currentUserData.getAvatar() != null && currentUserData.getUsername() != null) ||
                        (!currentUserData.getAvatar().isEmpty() && !currentUserData.getUsername().isEmpty());
        if (view != null) {
            view.isUserLoading(false);
            if (isUserViewVisible) view.getUserData(userData);
            view.setShowUserView(isUserViewVisible);
        }
    }

    @Override
    public void getProductsFromAPI(List<Product> products) {
        executorService.execute(() -> {
            this.products = products;
            gotDataFromAPI = true;
            productClassification(products);
        });
    }

    private void productClassification(List<Product> products) {
        getRecommendedEveryDay(products);
        getTopChartsProducts(products);
        getDiscountProducts(products);
        if (authenticated) interactor.getFavoriteProductFromFirebase();
        else getRecommendedProducts();
    }

    private void getRecommendedEveryDay(List<Product> products) {
        Collections.shuffle(products);
        recommendedEveryDay = products.subList(0, Math.min(products.size(), 10));
        if (view != null) {
            handler.post(() -> {
                view.isRecommendedProductsLoading(false);
                view.bindRecommendedEveryDay(recommendedEveryDay);
            });
        }
    }

    private void getTopChartsProducts(List<Product> products) {
        topChartsProducts = products.stream().filter(product -> product.getRating() > 4.8f).collect(Collectors.toList());
        if (view != null) {
            handler.post(() -> {
                view.isTopChartsProductsLoading(false);
                view.showTopChartsProducts(topChartsProducts);
            });
        }
    }

    private void getDiscountProducts(List<Product> products) {
        discountProducts = products.stream().filter(v -> v.getDiscountPercentage() > 12.00).collect(Collectors.toList());
        if (view != null) {
            handler.post(() -> {
                view.isDiscountProductsLoading(false);
                view.showDiscountProducts(discountProducts);
            });
        }
    }

    private void getRecommendedProducts() {
        List<Product> tempProducts = new ArrayList<>(this.products);
        Collections.shuffle(tempProducts);
        recommendedProducts = tempProducts.subList(0, Math.min(tempProducts.size(), 30));
        if (view != null) {
            handler.post(() -> {
                view.refreshInvisible();
                view.isRecommendedProductsLoading(false);
                view.showRecommendedProducts(recommendedProducts);
            });
        }
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
        if (view != null) {
            handler.post(() -> {
                view.isRecommendedProductsLoading(false);
                view.showRecommendedProducts(recommendedProducts);
                view.refreshInvisible();
            });
        }
    }

    @Override
    public void isUserDataEmpty() {
        gotUserData = true;
        currentUserData = null;
        if (view != null) {
            view.isUserLoading(false);
            view.setShowUserView(false);
        }
    }

    @Override
    public void isFavoriteProductEmpty() {
        getRecommendedProducts();
    }
}
