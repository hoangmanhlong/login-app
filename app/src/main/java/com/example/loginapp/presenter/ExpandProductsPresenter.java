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

    @StringRes private int label;

    private ExpandProductsView view;

    private List<Product> products;

    private SortStatus status;

    public ExpandProductsPresenter(ExpandProductsView view) {
        this.view = view;
    }

    public void clear() {
        products = null;
        status = null;
        view = null;
    }

    public void initData() {
        if (products == null || products.isEmpty()) view.getSharedData();
        else {
            sortProduct(status);
            view.bindScreenLabel(label);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products.getProducts();
        sortProduct(SortStatus.PRICE_LOW_TO_HIGH);
    }

    public void setLabel(int label) {
        this.label = label;
        view.bindScreenLabel(label);
    }

    public void sortProduct(SortStatus status) {
        this.status = status;
        setLabel(status);

        switch (status) {
            case PRICE_HIGH_TO_LOW:
                products = products.stream()
                        .sorted(Comparator.comparingInt(Product::getPrice).reversed())
                        .collect(Collectors.toList());
                break;
            case PRICE_LOW_TO_HIGH:
                products = products.stream()
                        .sorted(Comparator.comparingDouble(Product::getPrice))
                        .collect(Collectors.toList());
                break;
            case RATE_LOW_TO_HIGH:
                products = products.stream()
                        .sorted(Comparator.comparingDouble(Product::getRating))
                        .collect(Collectors.toList());
                break;
            case RATE_HIGH_TO_LOW:
                products = products.stream()
                        .sorted(Comparator.comparingDouble(Product::getRating).reversed())
                        .collect(Collectors.toList());
                break;
        }
        view.getProducts(products);
    }

    private void setLabel(SortStatus status) {
        int statusLabel = 0;
        switch (status) {
            case PRICE_HIGH_TO_LOW:
                statusLabel = R.string.price_high_to_low;
                break;
            case PRICE_LOW_TO_HIGH:
                statusLabel = R.string.price_low_to_high;
                break;
            case RATE_LOW_TO_HIGH:
                statusLabel = R.string.rate_low_to_high;
                break;
            case RATE_HIGH_TO_LOW:
                statusLabel = R.string.rate_high_to_low;
                break;
        }
        if (statusLabel != 0) view.setSortStatusName(statusLabel);
    }
}
