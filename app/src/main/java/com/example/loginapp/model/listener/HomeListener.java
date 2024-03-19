package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;

import java.util.List;

public interface HomeListener {

    void getUserData(UserData userData);

    void getProductsFromAPI(List<Product> products);

    void onMessage(String message);

    void getFavoriteProducts(List<Product> products);

    void getBestsellerProducts(List<Product> products);

    void isUserDataEmpty();
}
