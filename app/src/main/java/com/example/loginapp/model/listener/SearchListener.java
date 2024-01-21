package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;

import java.util.List;

public interface SearchListener {
    void onLoadProducts(List<Product> products);

    void notifyItemAdded(SearchHistory history);

    void notifyItemRemoved(SearchHistory history);

    void onLoadError(String message);

    void showProcessBar(Boolean show);

    void onListEmpty(Boolean show);
}
