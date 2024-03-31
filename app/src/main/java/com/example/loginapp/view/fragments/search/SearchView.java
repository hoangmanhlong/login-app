package com.example.loginapp.view.fragments.search;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;

import java.util.List;

public interface SearchView {

    void bindSearchHistories(List<SearchHistory> searchHistories);

    void isSearchResultsViewVisible(boolean visible);

    void bindProducts(List<Product> products);

    void isSearchResultsEmpty(boolean isEmpty);

    void getSearchSuggestions(List<String> list);

    void hideSearchSuggestionsDropdown();

    void setIsSearchHistoriesViewVisible(boolean visible);

    void clearQueryButtonVisible(boolean visible);
}
