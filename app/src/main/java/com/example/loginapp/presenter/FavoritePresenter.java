package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.FavoriteInterator;
import com.example.loginapp.model.listener.FavoriteListener;
import com.example.loginapp.view.fragment.favorite_product.FavoriteView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritePresenter implements FavoriteListener {

    private final FavoriteInterator interator = new FavoriteInterator(this);

    private final FavoriteView view;

    public List<Product> wishlist = new ArrayList<>();

    public FavoritePresenter(FavoriteView view) {
        this.view = view;
    }

    public void initData() {
        wishlistState();
        if (wishlist.size() == 0) getFavoriteProducts();
        else view.onItemAdded(wishlist);
    }

    public void getFavoriteProducts() {
        interator.getFavoriteProductFromFirebase();
    }

    @Override
    public void onItemAdded(Product product) {
        wishlist.add(product);
        wishlistState();
        view.onItemAdded(wishlist);
    }

    private void wishlistState() {
        view.isWishlistEmpty(wishlist.size() == 0);
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    @Override
    public void notifyItemRemoved(Product product) {
        int index = wishlist.indexOf(wishlist.stream().filter(p -> p.getId() == product.getId()).collect(Collectors.toList()).get(0));
        wishlist.remove(index);
        wishlistState();
        view.notifyItemRemoved(index);
    }

    public void deleteFavoriteProduct(int id) {
        interator.deleteProduct(id);
    }
}
