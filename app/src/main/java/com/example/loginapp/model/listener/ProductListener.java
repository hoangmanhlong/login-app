package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;

public interface ProductListener {
    void onGetProduct(Product apiProduct);

    void onMessage(String message);

    void enableFavorite(Boolean compare);

    void isLoading(Boolean loading);

    void removeSuccess();

    void saveToBasketSuccess();
}
