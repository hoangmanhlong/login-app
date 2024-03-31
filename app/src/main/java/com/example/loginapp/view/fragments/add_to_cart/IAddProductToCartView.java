package com.example.loginapp.view.fragments.add_to_cart;

import com.example.loginapp.model.entity.Product;

public interface IAddProductToCartView {

    void bindQuantity(String quantity);

    void bindProduct(Product product);

    void dismissFragment();
}
