package com.example.loginapp.view.fragment.product_favorite;

import com.example.loginapp.model.entity.Product;

public interface FavoriteView {
    void onItemAdded(Product products);
    void onMessage(String message);
}
