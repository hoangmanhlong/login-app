package com.example.loginapp.presenter;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.interator.MainInteractor;
import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.view.activities.MainView;
import com.google.firebase.auth.FirebaseAuth;

public class MainPresenter implements MainListener {

//    private final String TAG = MainPresenter.class.getName();

    private final MainView view;

    private final MainInteractor interactor = new MainInteractor(this);

    private final AppSharedPreferences sharedPreferences =
            AppSharedPreferences.getInstance(App.getInstance());

    public MainPresenter(MainView view) {
        this.view = view;
    }

    private void getWishlistStatus() {
        view.hasNewProductInFavoritesList(!sharedPreferences.getFavoritesListStatus());
    }

    public void viewedFavoritesList(boolean viewedFavoritesList) {
        sharedPreferences.saveViewedFavoritesListStatus(viewedFavoritesList);
        getWishlistStatus();
    }

    public void firebaseAuthState() {
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            boolean hasUser = firebaseAuth.getCurrentUser() != null;
            if (hasUser) {
                interactor.getNavigationState();
                getWishlistStatus();
            }
            view.hasUser(hasUser);
        });
    }

    @Override
    public void getNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        view.bindNumberOfProductInShoppingCart(number, isShoppingCartEmpty);
    }
}
