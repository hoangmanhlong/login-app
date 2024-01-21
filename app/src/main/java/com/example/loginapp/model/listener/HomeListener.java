package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.UserData;

import java.util.List;

public interface HomeListener {
    void getUserData(UserData userData);

    void onLoadProducts(List<Product> products);

    void onLoadError(String message);

    void onLoadCategories(List<String> categories);

    void showProcessBar(Boolean show);

}
