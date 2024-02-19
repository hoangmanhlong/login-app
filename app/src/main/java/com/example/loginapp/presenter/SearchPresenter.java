package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.interator.SearchProductInterator;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.view.fragments.search.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPresenter implements SearchListener {

    private final SearchProductInterator interator = new SearchProductInterator(this);

    private final SearchView view;

    public boolean isShowSearchResult = false;

    public List<SearchHistory> histories = new ArrayList<>();

    public List<Product> products = new ArrayList<>();

    public SearchPresenter(SearchView view) {
        this.view = view;
    }

    private List<String> categories = new ArrayList<>();

    public void initData() {
        if (histories.isEmpty()) getSearchHistories();
        else view.notifyItemAdded(histories);

        if (!products.isEmpty() && isShowSearchResult) {
            view.onLoadProducts(products);
            view.showSearchResult(true);
            listStatus();
        }

        if (categories.isEmpty()) getCategories();
        else view.getCategories(categories);
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

    private void getCategories() {
        interator.getCategories();
    }

    private void getSearchHistories() {
        interator.getSearchHistories();
    }

    @Override
    public void getProducts(List<Product> products) {
        setIsShowSearchResult(true);
        this.products = products;
        view.onLoadProducts(this.products);
        listStatus();
    }

    private void setIsShowSearchResult(boolean isShowSearchResult) {
        this.isShowSearchResult = isShowSearchResult;
        view.showSearchResult(isShowSearchResult);
    }

    private void listStatus() {
        view.isSearchEmpty(products.isEmpty());
    }

    public void onBtBackClick() {
        setIsShowSearchResult(false);
    }

    @Override
    public void notifyItemAdded(SearchHistory history) {
        histories.add(history);
        if (histories.size() > 1)
            histories.sort((history1, history2) -> Long.compare(history2.getTime(), history1.getTime()));
        view.notifyItemAdded(histories);
    }

    @Override
    public void notifyItemRemoved(SearchHistory history) {
        int index = histories.indexOf(histories.stream().filter(p -> p.getText().equals(history.getText())).collect(Collectors.toList()).get(0));
        histories.remove(index);
        view.notifyItemRemoved(index);
    }

    @Override
    public void onLoadError(String message) {
        view.onMessage(message);
    }

    @Override
    public void getCategories(List<String> categories) {
        this.categories = categories;
        view.getCategories(categories);
    }

    public void deleteHistory(String text) {
        interator.deleteSearchHistory(text);
    }

    private boolean isValidFirebasePath(String path) {
        return !path.contains(".") && !path.contains("#") && !path.contains("$") && !path.contains("[") && !path.contains("]");
    }
}
