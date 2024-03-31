package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.interator.SearchProductInteractor;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.view.fragments.search.SearchView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchListener {

    private static final String TAG = SearchPresenter.class.getSimpleName();

    private SearchProductInteractor interator;

    private SearchView view;

    // Màn hình có đang ở trạng thái show kết quả tìm kiếm không?
    public Boolean isShowSearchResult = false;

    // Lịch sử tìm kiếm đã được lấy từ Firebase hay chưa?
    private Boolean retrievedSearchHistories = false;

    // Người dùng đã xác thực hay chưa?
    // Mục đích: hiển thị lịch sử tìm kiếm của họ
    private Boolean authenticated;

    // Danh sách lịch sử tìm kiếm
    public List<SearchHistory> searchHistories;

    // Products từ kết quả tìm kiếm
    public List<Product> products;

    // Truy vấn hiện tại của người dùng
    private String query;

    public void detachView() { // Clear unused variable
        view = null;
        authenticated = null;
        isShowSearchResult = null;
        retrievedSearchHistories = null;
        interator.clearRef();
        interator = null;
        products = null;
        query = null;
        searchHistories = null;
    }

    public SearchPresenter(SearchView view) {
        this.view = view;
        interator = new SearchProductInteractor(this);
        authenticated = FirebaseAuth.getInstance().getCurrentUser() != null;
        searchHistories = new ArrayList<>();
        products = new ArrayList<>();
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
            view.isSearchResultsViewVisible(true);
            if (products.isEmpty()) {
                view.isSearchResultsEmpty(true);
            } else {
                view.isSearchResultsEmpty(false);
                view.bindProducts(this.products);
            }
        } else {
            view.isSearchResultsViewVisible(false);
        }
    }

    public void setQuery(String query) {
        this.query = query;
        view.clearQueryButtonVisible(!query.isEmpty());
    }

//    @SuppressLint("CheckResult")
//    public void getSearchSuggestions() {
//        App.getDatabase().dao().getProductsName()
//                .map(productNames -> {
//                    List<String> names = new ArrayList<>();
//                    for (ProductName productName : productNames) {
//                        names.add(productName.productName);
//                    }
//                    return names;
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(view::getSearchSuggestions);
//    }

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
            view.isSearchResultsViewVisible(true);
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
        view.isSearchResultsViewVisible(true);
        if (products.isEmpty()) {
            view.isSearchResultsEmpty(true);
        } else {
            view.isSearchResultsEmpty(false);
            view.bindProducts(products);
        }
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
        view.isSearchResultsViewVisible(false);
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
