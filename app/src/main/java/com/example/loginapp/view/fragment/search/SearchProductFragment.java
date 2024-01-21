package com.example.loginapp.view.fragment.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.adapter.favorite_adapter.FavoriteAdapter;
import com.example.loginapp.adapter.favorite_adapter.FavoriteItemClickListener;
import com.example.loginapp.adapter.seach_suggest_adapter.SearchSuggestAdapter;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.databinding.FragmentSearchProductBinding;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.presenter.SearchPresenter;
import com.example.loginapp.view.state.ClearButtonObserver;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class SearchProductFragment extends Fragment implements SearchView, FavoriteItemClickListener {

    private final SearchPresenter presenter = new SearchPresenter(this);

    private FragmentSearchProductBinding binding;

    private final SearchSuggestAdapter searchSuggestAdapter = new SearchSuggestAdapter(presenter);

    private RecyclerView recyclerView;

    private final FavoriteAdapter adapter = new FavoriteAdapter(this);

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = FragmentSearchProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        presenter.initData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        binding.setFragment(this);
        // Visibility navigation bar
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);

        // touch screen to hide keyboard
        view.setOnTouchListener((v, event) -> {
            hideKeyboardFrom(requireContext(), view);
            return false;
        });

        // Setup search histories
        recyclerView = binding.productRecyclerview;
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        RecyclerView searchSuggestionRecyclerview = binding.searchSuggestions;
        searchSuggestionRecyclerview.setLayoutManager(layoutManager);
        searchSuggestionRecyclerview.setAdapter(searchSuggestAdapter);
        searchSuggestAdapter.submitList(presenter.histories);
        binding.etQuery.addTextChangedListener(new ClearButtonObserver(binding.clearQuery, binding.etQuery));

        binding.etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.etQuery.getWindowToken(), 0);
                    presenter.onSearchProduct(binding.etQuery.getText().toString().trim());
                    binding.searchSuggestions.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showProcessBar(Boolean show) {
        if (show) {
            binding.loadSearchProcess.setVisibility(View.VISIBLE);
            binding.productRecyclerview.setVisibility(View.GONE);
        } else {
            binding.loadSearchProcess.setVisibility(View.GONE);
            binding.productRecyclerview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListEmpty(Boolean show) {
        if (show) {
            binding.titleEmpty.setVisibility(View.VISIBLE);
            binding.productRecyclerview.setVisibility(View.GONE);
        } else {
            binding.titleEmpty.setVisibility(View.GONE);
            binding.productRecyclerview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadProducts(List<Product> products) {
        adapter.submitList(products);
        recyclerView.setAdapter(adapter);
    }

    public void onClearClick() {
        binding.clearQuery.setVisibility(View.GONE);
        binding.etQuery.setText("");
        binding.productRecyclerview.setVisibility(View.GONE);
        binding.searchSuggestions.setVisibility(View.VISIBLE);
        binding.titleEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onLoadError(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putInt("productId", product.getId());
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_searchProductFragment_to_productFragment, bundle);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void searchWithHistory(String text) {
        binding.etQuery.setText(text);
        binding.searchSuggestions.setVisibility(View.GONE);
        binding.productRecyclerview.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void notifyItemAdded(List<SearchHistory> list) {
        searchSuggestAdapter.submitList(list);
//        searchSuggestAdapter.notifyItemInserted(list.size() - 1);
        searchSuggestAdapter.notifyDataSetChanged();
    }


    @Override
    public void notifyItemRemoved(int index) {
        searchSuggestAdapter.notifyItemRemoved(index);
    }

    @Override
    public void searchState(boolean check) {

    }
}
