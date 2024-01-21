package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;

public interface FavoriteListener {
    void onItemAdded(Product products);

    void onMessage(String message);
}
