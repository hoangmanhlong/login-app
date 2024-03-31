package com.example.loginapp.view.fragments.home;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;

import java.util.List;

public interface HomeView {

    void getUserData(UserData userData);

    void refreshInvisible();

    void showRecommendedProducts(List<Product> products);

    void showTopChartsProducts(List<Product> products);

    void showDiscountProducts(List<Product> products);

    void isRecommendedProductsLoading(boolean isLoading);

    void isTopChartsProductsLoading(boolean isLoading);

    void isDiscountProductsLoading(boolean isLoading);

    void isUserLoading(boolean isLoading);

    void bindRecommendedEveryDay(List<Product> products);

    void setShowUserView(Boolean show);
}
