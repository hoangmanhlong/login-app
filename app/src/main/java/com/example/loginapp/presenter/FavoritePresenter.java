package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.FavoriteInterator;
import com.example.loginapp.model.listener.FavoriteListener;
import com.example.loginapp.view.fragment.product_favorite.FavoriteView;

public class FavoritePresenter implements FavoriteListener {

    private FavoriteInterator interator;

    private FavoriteView view;

    public FavoritePresenter(FavoriteView view) {
        this.view = view;
        interator = new FavoriteInterator(this);

    }

    public void getFavoriteProducts() {
        interator.getFavoriteProductFromFirebase();
    }

    @Override
    public void onItemAdded(Product products) {
        view.onItemAdded(products);
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }
}
