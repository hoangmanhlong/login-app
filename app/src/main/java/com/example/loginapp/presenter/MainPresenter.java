package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.interator.MainInteractor;
import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.view.activities.MainView;

public class MainPresenter implements MainListener {

    private final MainInteractor interactor = new MainInteractor(this);

    private final MainView view;

    private int numberOfBasket = -1;

    private int numberOfWishlist = -1;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void getStatus(Activity activity) {
        interactor.getNumber(activity);
    }


    public void saveNumberOfProductInBasket(Activity activity) {
        AppSharedPreferences.getInstance(activity).saveNumberOfProductInBasket(numberOfBasket, true);
        view.notifyCartChanged(false);
    }

    public void saveNumberOfProductInWishlist(Activity activity) {
        AppSharedPreferences.getInstance(activity).saveNumberOfProductInBasket(numberOfWishlist, false);
        view.notifyFavoriteChanged(false);
    }

    @Override
    public void getNumberOfBasketFromServer(int number) {
        view.notifyCartChanged(true);
        this.numberOfBasket = number;
    }

    @Override
    public void getNumberOfWishlistFromServer(int number) {
        view.notifyFavoriteChanged(true);
        this.numberOfWishlist = number;
    }
}
