package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;

import java.util.List;

public interface HomeListener {

    void getUserData(UserData userData);

    void getProductsFromAPI(List<Product> products);

    void getFavoriteProducts(List<Product> products);

    void isUserDataEmpty();

    void isFavoriteProductEmpty();
}
