package com.example.loginapp.presenter;

import android.annotation.SuppressLint;

import com.example.loginapp.App;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.ProductName;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.interator.SearchProductInterator;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.view.fragments.search.SearchView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchListener {

    private final String TAG = SearchPresenter.class.getSimpleName();

    private SearchProductInterator interator;

    private SearchView view;

    public boolean isShowSearchResult = false;

    private boolean retrievedSearchHistories = false;

    private final boolean authenticated;

    private static List<String> searchSuggestions = new ArrayList<>();

    public List<SearchHistory> searchHistories = new ArrayList<>();

    public List<Product> products = new ArrayList<>();

    private String query;

    public void detachView() {
        view = null;
        interator.clearRef();
        interator = null;
        products = null;
        searchHistories = null;
    }

    public SearchPresenter(SearchView view) {
        this.view = view;
        interator = new SearchProductInterator(this);
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void initData() {
        if (authenticated) {
            if (retrievedSearchHistories) {
                if (searchHistories.isEmpty()) {
                    view.setIsSearchHistoriesViewVisible(false);
                } else {
                    view.setIsSearchHistoriesViewVisible(true);
                    view.bindSearchHistories(searchHistories);
                }
            }
        }

        if (isShowSearchResult) {
            if (products.isEmpty()) {
                view.isProductListEmpty(true);
            } else {
                view.isProductListEmpty(false);
                view.bindProducts(this.products);
            }
            view.isProductListVisible(true);
        }
    }

    public void setQuery(String query) {
        this.query = query;
        view.clearQueryButtonVisible(!query.isEmpty());
    }

    @SuppressLint("CheckResult")
    public void getSearchSuggestions() {
        App.getDatabase().dao().getProductsName()
                .map(productNames -> {
                    List<String> names = new ArrayList<>();
                    for (ProductName productName : productNames) {
                        names.add(productName.productName);
                    }
                    return names;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::getSearchSuggestions);
    }

    public void searchWithSearchHistory(String query) {
        this.query = query;
        searchProcess(query);
    }

    public void onActionSearchClick() {
        searchProcess(query);
    }

    private void searchProcess(String query) {
        if (query != null && !query.isEmpty() && isValidFirebasePath(query)) {
            interator.searchProduct(query);
            if (authenticated) interator.saveSearchHistory(query);
            view.isProductListVisible(true);
            view.hideSearchSuggestionsDropdown();
        }
    }

    public void addSearchHistoriesValueEventListener() {
        if (authenticated) interator.addSearchHistoriesValueEventListener();
    }

    public void removeSearchHistoriesValueEventListener() {
        if (authenticated) interator.removeSearchHistoriesValueEventListener();
    }

    @Override
    public void getProducts(List<Product> products) {
        this.products = products;
        isShowSearchResult = true;
        if (!products.isEmpty()) view.bindProducts(this.products);
        view.isProductListEmpty(products.isEmpty());
    }

    @Override
    public void getSearchHistories(List<SearchHistory> searchHistories) {
        this.searchHistories = searchHistories;
        if (searchHistories.size() > 1)
            this.searchHistories.sort((history1, history2) -> Long.compare(history2.getTime(), history1.getTime()));
        view.setIsSearchHistoriesViewVisible(true);
        view.bindSearchHistories(this.searchHistories);
        retrievedSearchHistories = true;
    }

    public void onBtBackClick() {
        isShowSearchResult = false;
        view.isProductListVisible(false);
    }

    @Override
    public void deleteSuccess(boolean isSuccess) {

    }

    @Override
    public void isSearchHistoriesEmpty() {
        retrievedSearchHistories = true;
        view.setIsSearchHistoriesViewVisible(false);
    }

    public void deleteHistory(String text) {
        interator.deleteSearchHistory(text);
    }

    public void deleteAllSearchHistories() {
        interator.deleteAllSearchHistories();
    }

    private boolean isValidFirebasePath(String path) {
        return !path.contains(".") && !path.contains("#") && !path.contains("$") && !path.contains("[") && !path.contains("]");
    }
}
