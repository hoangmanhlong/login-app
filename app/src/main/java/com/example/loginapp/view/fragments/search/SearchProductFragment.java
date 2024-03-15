package com.example.loginapp.view.fragments.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.loginapp.App;
import com.example.loginapp.R;
import com.example.loginapp.adapter.seach_suggest_adapter.OnSearchProductClickListener;
import com.example.loginapp.adapter.seach_suggest_adapter.OnSearchSuggestionClickListener;
import com.example.loginapp.adapter.seach_suggest_adapter.SearchProductAdapter;
import com.example.loginapp.adapter.seach_suggest_adapter.SearchSuggestAdapter;
import com.example.loginapp.databinding.FragmentSearchProductBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.presenter.SearchPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class SearchProductFragment extends Fragment implements SearchView, OnSearchProductClickListener, OnSearchSuggestionClickListener {

    private final SearchPresenter presenter = new SearchPresenter(this);

    private final String TAG = this.toString();

    private FragmentSearchProductBinding binding;

    private InputMethodManager imm;

    private final SearchSuggestAdapter searchSuggestAdapter = new SearchSuggestAdapter(this);

    private final SearchProductAdapter searchProductAdapter = new SearchProductAdapter(this);

    private AutoCompleteTextView etQueryBox;

    private final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
            App.getInstance().getApplicationContext(),
            android.R.layout.simple_list_item_1
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = FragmentSearchProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE); // Lấy instance của InputMethodManager
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null)
            if (imm.isAcceptingText())
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        etQueryBox = binding.etQuery;
        etQueryBox.setAdapter(arrayAdapter);

        RecyclerView recyclerView = binding.productRecyclerview;
        recyclerView.setAdapter(searchProductAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        RecyclerView searchSuggestionRecyclerview = binding.searchSuggestions;
        searchSuggestionRecyclerview.setLayoutManager(layoutManager);
        searchSuggestionRecyclerview.setAdapter(searchSuggestAdapter);
        ((SimpleItemAnimator) searchSuggestionRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);

        binding.etQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.onActionSearchClick();
                View currentFocus = requireActivity().getCurrentFocus();
                if (currentFocus != null) {
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
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
    public void onResume() {
        super.onResume();
        // Auto Show KeyBoard when Fragment show on Screen
        binding.etQuery.requestFocus(); // focus Query EditText
        imm.showSoftInput(binding.etQuery, 0); // Hiển thị bàn phím cho EditText
    }

    @Override
    public void bindSearchHistories(List<SearchHistory> searchHistories) {
        searchSuggestAdapter.submitList(searchHistories);
    }

    @Override
    public void isProductListVisible(boolean visible) {
        binding.setIsProductsViewVisible(visible);
    }

    @Override
    public void bindProducts(List<Product> products) {
        searchProductAdapter.submitList(products);
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void isProductListEmpty(boolean isEmpty) {
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
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_all_search_histories)
                .setPositiveButton(R.string.delete, (dialog, which) -> presenter.deleteAllSearchHistories())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onSeeMoreRecommendedProductsViewClick() {

    }

    @Override
    public void deleteSearchHistory(SearchHistory history) {
        presenter.deleteHistory(history.getText());
    }
}
