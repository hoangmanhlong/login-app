package com.example.loginapp.view.fragments.add_to_cart;

import androidx.annotation.StringRes;

import com.example.loginapp.model.entity.Product;

public interface AddProductToCartView {

    void bindQuantity(String quantity);

    void getSharedData();

    void bindProduct(Product product);

    void dismissAddProductToCartFragment();

    void onMessage(@StringRes int message);
}
