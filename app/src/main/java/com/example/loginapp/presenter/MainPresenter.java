package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.App;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.view.activities.MainView;
import com.google.firebase.auth.FirebaseAuth;

public class MainPresenter {

    private final String TAG = MainPresenter.class.getName();

    private final MainView view;

    private final AppSharedPreferences sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void getStatus() {
        getBasketStatus();
        getWishlistStatus();
    }

    public void getBasketStatus() {
        view.notifyCartChanged(sharedPreferences.getNumberOfProductInBasket());
    }

    public void getWishlistStatus() {
        view.notifyFavoriteChanged(sharedPreferences.getNumberOfProductInWishlist());
    }

    public void setViewedShoppingCart(boolean viewedShoppingCart) {
        sharedPreferences.saveViewedStatus(Constant.IS_VIEWED_SHOPPING_CART_KEY, viewedShoppingCart);
        getBasketStatus();
    }

    public void setViewedFavoritesList(boolean viewedFavoritesList) {
        sharedPreferences.saveViewedStatus(Constant.IS_VIEWED_FAVORITES_LIST_KEY, viewedFavoritesList);
        getWishlistStatus();
    }

    public void currentUserState() {
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            Log.d(TAG, "currentUserState: " + (firebaseAuth.getCurrentUser() != null));
            view.hasUser(firebaseAuth.getCurrentUser() != null);
        });
    }
}
