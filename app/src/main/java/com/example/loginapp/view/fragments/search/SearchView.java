package com.example.loginapp.view.fragments.search;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;

import java.util.List;

public interface SearchView {

    void onLoadProducts(List<Product> products);

    void onMessage(String message);

    void notifyItemAdded(List<SearchHistory> list);

    void notifyItemChanged(int index);

    void notifyItemRemoved(int index);

    void showSearchResult(Boolean show);

    void isSearchEmpty(boolean isEmpty);

    void getSearchSuggestions(List<String> list);

    void hideSearchSuggestionsDropdown();

    void isSearchSuggestionViewVisible(boolean visible);

    void clearQueryButtonVisible(boolean visible);
}
