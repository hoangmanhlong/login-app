package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface CartListener {

    void getProductsFromShoppingCart(List<FirebaseProduct> products);

    void isCartEmpty();
}
