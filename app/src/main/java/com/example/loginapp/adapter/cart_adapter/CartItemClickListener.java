package com.example.loginapp.adapter.cart_adapter;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;

public interface CartItemClickListener {
    void onItemClick(Product product);

    void updateQuantity(int id, int quantity);

    void onItemChecked(FirebaseProduct product, boolean checked);

    void onDeleteProduct(FirebaseProduct product);
}
