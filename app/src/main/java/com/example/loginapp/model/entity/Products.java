package com.example.loginapp.model.entity;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
