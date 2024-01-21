package com.example.loginapp.adapter.cart_adapter;

import com.example.loginapp.model.entity.FirebaseProduct;

public interface CartItemClickListener {
    void onItemClick(int id);

    void updateQuantity(int id, int quantity);

    void onItemChecked(FirebaseProduct product, boolean checked);

    void onDeleteProduct(int id);
}
