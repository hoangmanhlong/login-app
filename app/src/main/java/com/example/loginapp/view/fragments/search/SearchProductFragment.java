package com.example.loginapp.view.fragments.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.loginapp.App;
import com.example.loginapp.R;
import com.example.loginapp.adapter.seach_suggest_adapter.OnSearchProductClickListener;
import com.example.loginapp.adapter.seach_suggest_adapter.OnSearchSuggestionClickListener;
import com.example.loginapp.adapter.seach_suggest_adapter.SearchHistoryAdapter;
import com.example.loginapp.adapter.seach_suggest_adapter.SearchProductAdapter;
import com.example.loginapp.databinding.FragmentSearchProductBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.presenter.SearchPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppConfirmDialog;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class SearchProductFragment
        extends Fragment
        implements SearchView, OnSearchProductClickListener, OnSearchSuggestionClickListener {

    private SearchPresenter presenter;

    private static final String TAG = SearchProductFragment.class.getSimpleName();

    private FragmentSearchProductBinding binding;

    private InputMethodManager inputMethodManager;

    private SearchHistoryAdapter searchHistoryAdapter;

    private SearchProductAdapter searchProductAdapter;

    private AutoCompleteTextView etQueryBox;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        presenter = new SearchPresenter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = FragmentSearchProductBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        etQueryBox = binding.etQuery;
        searchHistoryAdapter = new SearchHistoryAdapter(this);
        searchProductAdapter = new SearchProductAdapter(this);
        arrayAdapter = new ArrayAdapter<>(
                App.getInstance().getApplicationContext(),
                android.R.layout.simple_list_item_1
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE); // Lấy instance của InputMethodManager
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        etQueryBox.setAdapter(arrayAdapter);

        RecyclerView recyclerView = binding.productRecyclerview;
        recyclerView.setAdapter(searchProductAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        RecyclerView searchSuggestionRecyclerview = binding.searchSuggestions;
        searchSuggestionRecyclerview.setLayoutManager(layoutManager);
        searchSuggestionRecyclerview.setAdapter(searchHistoryAdapter);
        SimpleItemAnimator simpleItemAnimator = ((SimpleItemAnimator) searchSuggestionRecyclerview.getItemAnimator());
        if (simpleItemAnimator != null) simpleItemAnimator.setSupportsChangeAnimations(false);

        binding.etQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.onActionSearchClick();
                View currentFocus = requireActivity().getCurrentFocus();
                if (currentFocus != null) {
                    if (inputMethodManager.isAcceptingText()) {
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }
                return true;
            }
            return false;
        });

        binding.etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setQuery(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        presenter.initData();
        etQueryBox.setOnClickListener(c -> etQueryBox.showDropDown());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        etQueryBox = null;
        inputMethodManager = null;
        searchHistoryAdapter = null;
        searchProductAdapter = null;
        arrayAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addSearchHistoriesValueEventListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeSearchHistoriesValueEventListener();
    }

    @Override
    public void bindSearchHistories(List<SearchHistory> searchHistories) {
        searchHistoryAdapter.submitList(searchHistories);
    }

    @Override
    public void isSearchResultsViewVisible(boolean visible) {
        binding.setIsProductsViewVisible(visible);
    }

    @Override
    public void bindProducts(List<Product> products) {
        searchProductAdapter.submitList(products);
    }

    @Override
    public void isSearchResultsEmpty(boolean isEmpty) {
        binding.setIsProductListEmpty(isEmpty);
    }

    @Override
    public void getSearchSuggestions(List<String> list) {
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
    }

    @Override
    public void hideSearchSuggestionsDropdown() {
        etQueryBox.dismissDropDown();
    }

    @Override
    public void setIsSearchHistoriesViewVisible(boolean visible) {
        binding.setIsSearchHistoriesViewVisible(visible);
    }

    @Override
    public void clearQueryButtonVisible(boolean visible) {
        binding.setClearQueryButtonVisible(visible);
    }

    @Override
    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        NavHostFragment.findNavController(this).navigate(R.id.action_global_productFragment, bundle);
    }

    public void onClearQueryButtonClick() {
        binding.etQuery.setText("");
        presenter.setQuery("");
    }

    @Override
    public void onSearchSuggestClick(SearchHistory history) {
        binding.etQuery.setText(history.getText());
        presenter.searchWithSearchHistory(history.getText());
    }

    public void onBtBackClick() {
        presenter.onBtBackClick();
    }

    public void onDeleteAllSearchHistoriesButtonClick() {
        AppConfirmDialog.show(
                requireContext(),
                getString(R.string.delete),
                getString(R.string.delete_all_search_histories),
                new AppConfirmDialog.AppConfirmDialogButtonListener() {
                    @Override
                    public void onPositiveButtonClickListener() {
                        presenter.deleteAllSearchHistories();
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                }

        );
    }

    public void onSeeMoreRecommendedProductsViewClick() {

    }

    @Override
    public void deleteSearchHistory(SearchHistory history) {
        presenter.deleteHistory(history.getText());
    }
}
