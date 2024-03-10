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
import androidx.navigation.Navigation;
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

    private final String TAG = this.toString();

    private final ExpandProductsPresenter presenter = new ExpandProductsPresenter(this);

    private FragmentExpandProductsBinding binding;

    private final ExpandAdapter expandAdapter = new ExpandAdapter(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExpandProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        binding.recommendedRecyclerview.setAdapter(expandAdapter);
        getDataShared();
        binding.sortButton.setOnClickListener(v -> showMenu(v, R.menu.sort_menu));
    }

    private void getDataShared() {
        if (getArguments() != null) {
            @StringRes int label = getArguments().getInt(Constant.EXPAND_LABEL_KEY);
            binding.fragmentLabelTextView.setText(label);

            Products products = (Products) getArguments().getSerializable(Constant.EXPAND_PRODUCTS_KEY);
            if (products != null) {
                if (presenter.getProducts() == null) presenter.setProducts(products);
                else presenter.initData();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
//        if (popupMenu.getMenu() instanceof MenuBuilder) {
//            MenuBuilder menuBuilder = (MenuBuilder) popupMenu.getMenu();
//            menuBuilder.setOptionalIconsVisible(true);
//            for (MenuItem item : menuBuilder.getVisibleItems()) {
//
//                float iconMarginPx = TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_DIP, (float) ICON_MARGIN, getResources().getDisplayMetrics());
//                int iconMarginPxInt = (int) iconMarginPx;
//
//                if (item.getIcon() != null) {
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                        item.setIcon(new InsetDrawable(item.getIcon(), iconMarginPxInt, 0, iconMarginPxInt, 0));
//                    } else {
//                        item.setIcon(new InsetDrawable(item.getIcon(), iconMarginPxInt, 0, iconMarginPxInt, 0) {
//                            @Override
//                            public int getIntrinsicWidth() {
//                                return super.getIntrinsicWidth() + iconMarginPxInt + iconMarginPxInt;
//                            }
//                        });
//                    }
//                }
//            }
//        }

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.price_high_to_low) sortProductBy(SortStatus.PRICE_HIGH_TO_LOW);
            if (id == R.id.price_low_to_high) sortProductBy(SortStatus.PRICE_LOW_TO_HIGH);
            if (id == R.id.rate_high_to_low) sortProductBy(SortStatus.RATE_HIGH_TO_LOW);
            if (id == R.id.rate_low_to_high) sortProductBy(SortStatus.RATE_LOW_TO_HIGH);
            return false;
        });
        popupMenu.show();
    }


    private void sortProductBy(SortStatus status) {
        presenter.sortProduct(status);
    }

    @Override
    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        NavHostFragment.findNavController(this).navigate(R.id.action_global_productFragment, bundle);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void getProducts(List<Product> products) {
        expandAdapter.submitList(products);
    }

    @Override
    public void setSortStatusName(@StringRes int res) {
        binding.setSortName(res);
    }
}