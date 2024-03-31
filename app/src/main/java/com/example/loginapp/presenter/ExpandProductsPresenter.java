package com.example.loginapp.presenter;

import androidx.annotation.StringRes;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Products;
import com.example.loginapp.view.fragments.expand_products.ExpandProductsView;
import com.example.loginapp.view.fragments.expand_products.SortStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExpandProductsPresenter {

    private Boolean retrievedData = false;

    @StringRes private Integer screenLabel;

    @StringRes private Integer sortStatusLabel;

    private ExpandProductsView view;

    private List<Product> products;

    private SortStatus status;

    public ExpandProductsPresenter(ExpandProductsView view) {
        this.view = view;
        status = SortStatus.PRICE_LOW_TO_HIGH;
    }

    public void clear() {
        products = null;
        status = null;
        view = null;
        screenLabel = null;
        sortStatusLabel = null;
        retrievedData = null;
    }

    public void initData() {
        if (retrievedData) {
            sortProduct(status);
            view.bindScreenLabel(screenLabel);
        } else {
            view.getSharedData();
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products.getProducts();
        sortProduct(status);
        retrievedData = true;
    }

    public void setScreenLabel(int screenLabel) {
        this.screenLabel = screenLabel;
        view.bindScreenLabel(screenLabel);
    }

    public void sortProduct(SortStatus status) {
        this.status = status;
        switch (status) {
            case PRICE_HIGH_TO_LOW:
                sortStatusLabel = R.string.price_high_to_low;
                products = products.stream()
                        .sorted(Comparator.comparingInt(Product::getPrice).reversed())
                        .collect(Collectors.toList());
                break;
            case PRICE_LOW_TO_HIGH:
                sortStatusLabel = R.string.price_low_to_high;
                products = products.stream()
                        .sorted(Comparator.comparingDouble(Product::getPrice))
                        .collect(Collectors.toList());
                break;
            case RATE_LOW_TO_HIGH:
                sortStatusLabel = R.string.rate_low_to_high;
                products = products.stream()
                        .sorted(Comparator.comparingDouble(Product::getRating))
                        .collect(Collectors.toList());
                break;
            case RATE_HIGH_TO_LOW:
                sortStatusLabel = R.string.rate_high_to_low;
                products = products.stream()
                        .sorted(Comparator.comparingDouble(Product::getRating).reversed())
                        .collect(Collectors.toList());
                break;
        }
        view.setSortStatusLabel(sortStatusLabel);
        view.getProducts(products);
    }
}
