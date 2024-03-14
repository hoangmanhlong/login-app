package com.example.loginapp.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.loginapp.App;
import com.example.loginapp.data.local.room.AppDatabase;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.ProductName;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.interator.SearchProductInterator;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.view.fragments.search.SearchView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements SearchListener {

    private final String TAG = this.toString();

    private final AppDatabase database = App.getDatabase();

    private final SearchProductInterator interator = new SearchProductInterator(this);

    private final SearchView view;

    public boolean isShowSearchResult = false;

    private final boolean authenticated;

    private static List<String> searchSuggestions = new ArrayList<>();

    public List<SearchHistory> searchHistories = new ArrayList<>();

    public List<Product> products = new ArrayList<>();

    private String query;

    public SearchPresenter(SearchView view) {
        this.view = view;
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void initData() {
        if (authenticated) {
            if (searchHistories.isEmpty()) getSearchHistories();
            else {
                view.notifyItemAdded(searchHistories);
                view.isSearchSuggestionViewVisible(true);
            }
        }

        if (!products.isEmpty() && isShowSearchResult) {
            view.onLoadProducts(products);
            view.showSearchResult(true);
            listStatus();
        }

        if (searchSuggestions.isEmpty()) getSearchSuggestions();
        else view.getSearchSuggestions(searchSuggestions);
    }

    public void setQuery(String query) {
        this.query = query;
        view.clearQueryButtonVisible(!query.isEmpty());
    }

    @SuppressLint("CheckResult")
    public void getSearchSuggestions() {
        database.dao().getProductsName()
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

    public void onSearchProduct(String query) {
        if (!query.isEmpty() && isValidFirebasePath(query)) {
            products.clear();
            interator.searchProduct(query);
            if (authenticated) interator.saveSearchHistory(query);
            view.hideSearchSuggestionsDropdown();
        } else {
            view.onMessage("Invalid information");
        }
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
        view.isSearchSuggestionViewVisible(true);
        searchHistories.add(history);
        if (searchHistories.size() > 1)
            searchHistories.sort((history1, history2) -> Long.compare(history2.getTime(), history1.getTime()));
        view.notifyItemAdded(searchHistories);
    }

    @Override
    public void notifyItemChanged(SearchHistory history) {
        int index = searchHistories.indexOf(searchHistories.stream().filter(p -> p.getText().equals(history.getText())).collect(Collectors.toList()).get(0));
        searchHistories.set(index, history);
        searchHistories.sort((history1, history2) -> Long.compare(history2.getTime(), history1.getTime()));
        view.notifyItemChanged(index);
    }

    @Override
    public void notifyItemRemoved(SearchHistory history) {
        int index = searchHistories.indexOf(searchHistories.stream().filter(p -> p.getTime().equals(history.getTime())).collect(Collectors.toList()).get(0));
        searchHistories.remove(index);
        view.isSearchSuggestionViewVisible(searchHistories.size() > 0);
        view.notifyItemRemoved(index);
    }

    @Override
    public void onLoadError(String message) {
        view.onMessage(message);
    }

    @Override
    public void deleteSuccess(boolean isSuccess) {

    }

    @Override
    public void isSearchHistoriesEmpty() {
        view.isSearchSuggestionViewVisible(false);
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

//    @SuppressLint("StaticFieldLeak")
//    public class GetProductNamesAsyncTask extends AsyncTask<Void, Void, List<ProductName>> {
//
//        private final AppDao dao;
//
//        public GetProductNamesAsyncTask(AppDao dao) {
//            this.dao = dao;
//        }
//
//        @Override
//        protected List<ProductName> doInBackground(Void... voids) {
//            return dao.getProductsName();
//        }
//
//        @Override
//        protected void onPostExecute(List<ProductName> productNames) {
//            List<String> name = new ArrayList<>();
//            for (ProductName productName : productNames)
//                name.add(productName.productName);
//            searchSuggestions = name;
//            view.getSearchSuggestions(searchSuggestions);
//        }
//    }
}
