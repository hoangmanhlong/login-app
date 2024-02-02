package com.example.loginapp.presenter;

import com.example.loginapp.adapter.seach_suggest_adapter.OnSearchSuggestionClickListener;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.interator.SearchProductInterator;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.view.fragment.search.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPresenter implements SearchListener, OnSearchSuggestionClickListener {


    private final SearchProductInterator interator = new SearchProductInterator(this);

    private final SearchView view;

    public List<SearchHistory> histories = new ArrayList<>();

    public List<Product> products = new ArrayList<>();

    public SearchPresenter(SearchView view) {
        this.view = view;
    }

    public void initData() {
        if (histories.size() == 0) getSearchHistories();
        else view.notifyItemAdded(histories);
    }

    public void onSearchProduct(String query) {
        if (!query.isEmpty() && isValidFirebasePath(query)) {
            products.clear();
            interator.searchProduct(query);
            interator.saveSearchHistory(query);
        } else {
            view.onMessage("Invalid information");
        }
    }

    private void getSearchHistories() {
        interator.getSearchHistories();
    }

    @Override
    public void onLoadProducts(List<Product> products) {
        this.products = products;
        view.onLoadProducts(this.products);
    }

    @Override
    public void notifyItemAdded(SearchHistory history) {
        histories.add(history);
        histories.sort((history1, history2) -> Long.compare(history2.getTime(), history1.getTime()));
        view.notifyItemAdded(histories);
    }

    @Override
    public void notifyItemRemoved(SearchHistory history) {
        int index = histories.indexOf(histories.stream().filter(p -> p.getTime().equals(history.getTime())).collect(Collectors.toList()).get(0));
        histories.remove(index);
        view.notifyItemRemoved(index);
    }

    @Override
    public void onLoadError(String message) {
        view.onMessage(message);
    }

    @Override
    public void showProcessBar(Boolean show) {
        view.showProcessBar(show);
    }

    @Override
    public void onListEmpty(Boolean show) {
        view.onListEmpty(show);
    }

    @Override
    public void onSearchSuggestClick(SearchHistory history) {
        view.searchWithHistory(history.getText());
        interator.searchProduct(history.getText());
    }

    @Override
    public void deleteSearchHistory(SearchHistory history) {
        interator.deleteSearchHistory(history.getText());
    }

    private boolean isValidFirebasePath(String path) {
        return !path.contains(".") && !path.contains("#") && !path.contains("$") && !path.contains("[") && !path.contains("]");
    }
}
