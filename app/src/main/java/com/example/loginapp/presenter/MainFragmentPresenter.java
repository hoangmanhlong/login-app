package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.interator.MainInteractor;
import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.view.fragments.main_fragment.MainFragmentView;

public class MainFragmentPresenter implements MainListener {
    private final MainFragmentView view;

    private final MainInteractor interactor = new MainInteractor(this);

    private final AppSharedPreferences sharedPreferences;

    public MainFragmentPresenter(MainFragmentView view) {
        this.view = view;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
    }

    private void getWishlistStatus() {
        view.hasNewProductInFavoritesList(!sharedPreferences.getFavoritesListStatus());
    }

    public void viewedFavoritesList(boolean viewedFavoritesList) {
        sharedPreferences.saveViewedFavoritesListStatus(viewedFavoritesList);
        getWishlistStatus();
    }

    public void addValueEventListener() {
        getWishlistStatus();
        interactor.addValueEventListener();
    }

    public void removeValueEventListener() {
        interactor.removeValueEventListener();
    }

    @Override
    public void getNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        view.bindNumberOfProductInShoppingCart(number, isShoppingCartEmpty);
    }
}
