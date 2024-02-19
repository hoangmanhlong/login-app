package com.example.loginapp.view.fragments.favorite_product;

import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface FavoriteView {

    void onItemAdded(List<Product> products);

    void onMessage(String message);

    void notifyItemRemoved(int index);

    void isWishlistEmpty(Boolean isEmpty);
}
