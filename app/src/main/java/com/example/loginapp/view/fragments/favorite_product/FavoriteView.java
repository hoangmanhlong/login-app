package com.example.loginapp.view.fragments.favorite_product;

import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface FavoriteView {

    void onMessage(String message);

    void isWishlistEmpty(Boolean isEmpty);

    void bindFavoriteListProduct(List<Product> products);
}
