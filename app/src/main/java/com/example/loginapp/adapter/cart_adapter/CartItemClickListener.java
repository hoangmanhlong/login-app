package com.example.loginapp.adapter.cart_adapter;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;

public interface CartItemClickListener {
    void onItemClick(Product product);

    void onProductLongClick(Product product);

    void updateQuantity(int id, int quantity);

    void onCheckboxClick(FirebaseProduct product);

    void onDeleteProduct(int productId);
}
