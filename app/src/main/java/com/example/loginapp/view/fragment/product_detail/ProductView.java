package com.example.loginapp.view.fragment.product_detail;

import com.example.loginapp.model.entity.Product;

public interface ProductView {
    void onLoadProduct(Product product);

    void onMessage(String message);

    void enableFavorite(Boolean compare);

    void isLoading(Boolean loading);

    void saveToBasketSuccess();


}
