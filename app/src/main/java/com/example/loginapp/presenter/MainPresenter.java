package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.interator.MainInteractor;
import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.view.activities.MainView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPresenter implements MainListener {

//    private final String TAG = MainPresenter.class.getName();

    private final MainView view;

    private FirebaseAuth.AuthStateListener authStateListener;

    private final MainInteractor interactor = new MainInteractor(this);

    private final AppSharedPreferences sharedPreferences;

    public MainPresenter(MainView view) {
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

    public void getDataOnNavigationBar(FirebaseUser firebaseUser) {
        interactor.getNavigationState(firebaseUser.getUid());
        getWishlistStatus();
    }

    public void registerAuthStateListener() {
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            boolean isLogged = user != null;
            if (isLogged) {
                getDataOnNavigationBar(user);
            }
            view.hasUser(isLogged);
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    public void unregisterAuthStateListener() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    @Override
    public void getNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        view.bindNumberOfProductInShoppingCart(number, isShoppingCartEmpty);
    }
}
