package com.example.loginapp.view.fragments.expand_products;

import androidx.annotation.StringRes;

import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface ExpandProductsView {
    void getProducts(List<Product> products);

    void setSortStatusName(@StringRes int res);

    void getSharedData();

    void bindScreenLabel(@StringRes int label);
}
