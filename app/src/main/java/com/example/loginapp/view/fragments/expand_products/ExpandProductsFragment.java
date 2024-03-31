package com.example.loginapp.view.fragments.expand_products;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.adapter.expand_adapter.ExpandAdapter;
import com.example.loginapp.adapter.expand_adapter.ExpandProductClickListener;
import com.example.loginapp.databinding.FragmentExpandProductsBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Products;
import com.example.loginapp.presenter.ExpandProductsPresenter;
import com.example.loginapp.utils.Constant;

import java.util.List;


public class ExpandProductsFragment extends Fragment implements ExpandProductClickListener, ExpandProductsView {

    private static final String TAG = ExpandProductsFragment.class.getSimpleName();

    private ExpandProductsPresenter presenter;

    private NavController navController;

    private FragmentExpandProductsBinding binding;

    private ExpandAdapter expandAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new ExpandProductsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExpandProductsBinding.inflate(inflater, container, false);
        expandAdapter = new ExpandAdapter(this);
        binding.recommendedRecyclerview.setAdapter(expandAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initData();
        binding.sortButton.setOnClickListener(v -> showMenu(v, R.menu.sort_menu));
        binding.navigateUpButton.setOnClickListener(c -> navController.navigateUp());
    }

    @Override
    public void getSharedData() {
        if (getArguments() != null) {
            @StringRes int label = getArguments().getInt(Constant.EXPAND_LABEL_KEY);
            presenter.setScreenLabel(label);

            Products products = (Products) getArguments().getSerializable(Constant.EXPAND_PRODUCTS_KEY);
            if (products != null) presenter.setProducts(products);
        }
    }

    @Override
    public void bindScreenLabel(@StringRes int screenLabel) {
        binding.fragmentLabelTextView.setText(screenLabel);
    }

    @SuppressLint("RestrictedApi")
    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            SortStatus status = null;
            final int id = item.getItemId();
            if (id == R.id.price_high_to_low) status = SortStatus.PRICE_HIGH_TO_LOW;
            if (id == R.id.price_low_to_high) status = SortStatus.PRICE_LOW_TO_HIGH;
            if (id == R.id.rate_high_to_low) status = SortStatus.RATE_HIGH_TO_LOW;
            if (id == R.id.rate_low_to_high) status = SortStatus.RATE_LOW_TO_HIGH;
            if (status != null) presenter.sortProduct(status);
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        navController.navigate(R.id.action_global_productFragment, bundle);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        expandAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
        navController = null;
    }

    @Override
    public void getProducts(List<Product> products) {
        expandAdapter.submitList(products);
    }

    @Override
    public void setSortStatusLabel(@StringRes int sortStatusLabel) {
        binding.sortNameChip.setText(sortStatusLabel);
    }
}