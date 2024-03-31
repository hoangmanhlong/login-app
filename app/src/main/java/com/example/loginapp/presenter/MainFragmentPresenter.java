package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.interator.MainInteractor;
import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.view.fragments.main_fragment.MainFragmentView;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragmentPresenter implements MainListener {

    private static final String TAG = MainFragmentPresenter.class.getSimpleName();

    private MainFragmentView view;

    private MainInteractor interactor;

    private final boolean logged;

    private AppSharedPreferences sharedPreferences;

    public void clear() {
        view = null;
        interactor.clear();
        interactor = null;
        sharedPreferences = null;
    }

    public MainFragmentPresenter(MainFragmentView view) {
        this.view = view;
        interactor = new MainInteractor(this);
        logged = FirebaseAuth.getInstance().getUid() != null;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
    }

    public void initData() {
        view.setAdapter(logged);
        if (!logged) view.showLoginPopup();
    }

    private void getWishlistStatus() {
        view.hasNewProductInFavoritesList(!sharedPreferences.getFavoritesListStatus());
    }

    public void viewedFavoritesList(boolean viewedFavoritesList) {
        sharedPreferences.saveViewedFavoritesListStatus(viewedFavoritesList);
        getWishlistStatus();
    }

    public void addValueEventListener() {
        if (logged) {
            getWishlistStatus();
            interactor.addValueEventListener();
        }
    }

    public void removeValueEventListener() {
        if (logged) interactor.removeValueEventListener();
    }

    @Override
    public void getNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        view.bindNumberOfProductInShoppingCart(number, isShoppingCartEmpty);
    }
}
