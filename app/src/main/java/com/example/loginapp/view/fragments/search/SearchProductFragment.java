package com.example.loginapp.view.fragments.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

public class SearchProductFragment extends Fragment implements SearchView, OnSearchProductClickListener, OnSearchSuggestionClickListener {

    private final SearchPresenter presenter = new SearchPresenter(this);

    private FragmentSearchProductBinding binding;

    private final SearchSuggestAdapter searchSuggestAdapter = new SearchSuggestAdapter(this);

    private final SearchProductAdapter adapter = new SearchProductAdapter(this);

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
            App.getInstance().getApplicationContext(), android.R.layout.simple_list_item_1
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
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        RecyclerView recyclerView = binding.productRecyclerview;

        recyclerView.setAdapter(adapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        RecyclerView searchSuggestionRecyclerview = binding.searchSuggestions;
        searchSuggestionRecyclerview.setLayoutManager(layoutManager);
        searchSuggestionRecyclerview.setAdapter(searchSuggestAdapter);

        presenter.initData();
        binding.etQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.onSearchProduct(binding.etQuery.getText().toString());
                View currentFocus = requireActivity().getCurrentFocus();
                if (currentFocus != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isAcceptingText()) {
                        inputMethodManager.hideSoftInputFromWindow(
                                currentFocus.getWindowToken(),
                                0
                        );
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
                if (s.toString().trim().length() != 0)
                    binding.btClearQuery.setVisibility(View.VISIBLE);
                else {
                    binding.btClearQuery.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onLoadProducts(List<Product> products) {
        adapter.submitList(products);
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void notifyItemAdded(List<SearchHistory> list) {
        searchSuggestAdapter.submitList(list);
        searchSuggestAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemRemoved(int index) {
        searchSuggestAdapter.notifyItemRemoved(index);
    }

    @Override
    public void showSearchResult(Boolean show) {
        if (show) {
            binding.searchSuggestions.setVisibility(View.GONE);
            binding.productRecyclerview.setVisibility(View.VISIBLE);
            binding.btBack.setVisibility(View.VISIBLE);
        } else {
            binding.searchSuggestions.setVisibility(View.VISIBLE);
            binding.productRecyclerview.setVisibility(View.GONE);
            binding.btBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void isSearchEmpty(boolean isEmpty) {
        if (isEmpty) {
            binding.productRecyclerview.setVisibility(View.GONE);
            binding.tvListEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.productRecyclerview.setVisibility(View.VISIBLE);
            binding.tvListEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void getCategories(List<String> categories) {
        arrayAdapter.clear();
        arrayAdapter.addAll(categories);
        binding.etQuery.setAdapter(arrayAdapter);
    }

    @Override
    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_productFragment, bundle);
    }

    public void onClearQueryButtonClick() {
        binding.etQuery.setText("");
        binding.btClearQuery.setVisibility(View.GONE);
    }

    @Override
    public void onSearchSuggestClick(SearchHistory history) {
        binding.etQuery.setText(history.getText());
        presenter.onSearchProduct(history.getText());
    }

    public void onBtBackClick() {
        presenter.onBtBackClick();
        binding.tvListEmpty.setVisibility(View.GONE);
    }

    @Override
    public void deleteSearchHistory(SearchHistory history) {
        presenter.deleteHistory(history.getText());
    }
}
