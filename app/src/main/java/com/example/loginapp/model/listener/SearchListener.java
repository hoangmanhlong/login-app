package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;

import java.util.List;

public interface SearchListener {

    void getProducts(List<Product> products);

    void getSearchHistories(List<SearchHistory> searchHistories);;

    void deleteSuccess(boolean isSuccess);

    void isSearchHistoriesEmpty();
}
