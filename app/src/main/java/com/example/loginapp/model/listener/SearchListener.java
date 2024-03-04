package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;

import java.util.List;

public interface SearchListener {

    void getProducts(List<Product> products);

    void notifyItemAdded(SearchHistory history);

    void notifyItemChanged(SearchHistory history);

    void notifyItemRemoved(SearchHistory history);

    void onLoadError(String message);

    void deleteSuccess(boolean isSuccess);

    void isSearchHistoriesEmpty();
}
