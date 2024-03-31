package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface FavoriteListener {

    void isWishlistEmpty();

    void bindFavoriteListProduct(List<Product> products);
}
