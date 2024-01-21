package com.example.loginapp.data.remote.api.dto;

import com.example.loginapp.model.entity.Product;

import java.util.List;

public class ProductResponse {
    private List<Product> products;
    private int total;
    private int skip;
    private int limit;

    public ProductResponse(List<Product> products, int total, int skip, int limit) {
        this.products = products;
        this.total = total;
        this.skip = skip;
        this.limit = limit;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getTotal() {
        return total;
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }
}
