package com.example.loginapp.view.fragment.search;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;

import java.util.List;

public interface SearchView {
    void onLoadProducts(List<Product> products);

    void onLoadError(String message);

    void showProcessBar(Boolean show);

    void onListEmpty(Boolean show);

    void searchWithHistory(String text);

    void notifyItemAdded(List<SearchHistory> list);

    void notifyItemRemoved(int index);

    void searchState(boolean check);
}
