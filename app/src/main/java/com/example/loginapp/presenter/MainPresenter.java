package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.data.Constant;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.view.activities.MainView;

public class MainPresenter {

    private final MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void getStatus(Activity activity) {
        getBasketStatus(activity);
        getWishlistStatus(activity);
    }

    public void getBasketStatus(Activity activity) {
        view.notifyCartChanged(AppSharedPreferences.getInstance(activity).getNumberOfProductInBasket());
    }

    public void getWishlistStatus(Activity activity) {
        view.notifyFavoriteChanged(AppSharedPreferences.getInstance(activity).getNumberOfProductInWishlist());
    }

    public void setViewedShoppingCart(Activity activity, boolean viewedShoppingCart) {
        AppSharedPreferences.getInstance(activity).saveViewedStatus(Constant.IS_VIEWED_SHOPPING_CART_KEY, viewedShoppingCart);
        getBasketStatus(activity);
    }

    public void setViewedFavoritesList(Activity activity, boolean viewedFavoritesList) {
        AppSharedPreferences.getInstance(activity).saveViewedStatus(Constant.IS_VIEWED_FAVORITES_LIST_KEY, viewedFavoritesList);
        getWishlistStatus(activity);
    }
}
