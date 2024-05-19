package com.example.loginapp.view.fragments.action_on_product;

import com.example.loginapp.model.entity.Product;

public interface ActionOnProductView {
    void bindProduct(Product product);

    void updateView(boolean openedFromCart);

    void removeProduct(int productId);

    void openAddToCartFragment(Product product);

    void dismissFragment();
}
