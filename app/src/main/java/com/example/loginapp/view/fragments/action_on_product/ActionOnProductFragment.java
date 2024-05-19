package com.example.loginapp.view.fragments.action_on_product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentActionOnProductBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.add_to_cart.AddProductToCartFragment;
import com.example.loginapp.view.fragments.cart.CartFragment;
import com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ActionOnProductFragment extends BottomSheetDialogFragment implements ActionOnProductView {

    private ActionOnProductPresenter presenter;

    private FragmentActionOnProductBinding binding;

    public static final String TAG = ActionOnProductFragment.class.getSimpleName();

    private Fragment caller;

    public ActionOnProductFragment(Fragment caller) {
        this.caller = caller;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ActionOnProductPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentActionOnProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btAdd.setOnClickListener(v -> presenter.addProduct());
        binding.btRemove.setOnClickListener(v -> presenter.removeProduct());
        getSharedData();
    }

    /**
     * Access Product sent from {@link CartFragment} or {@link FavoriteProductFragment}
     */
    public void getSharedData() {
        if (getArguments() != null) {
            presenter.setOpenedFromCart(getArguments().getBoolean(Constant.OPENED_FROM_CART));
            Product product = (Product) getArguments().getSerializable(Constant.PRODUCT_KEY);
            if (product != null) presenter.setProduct(product);
        }
    }

    @Override
    public void bindProduct(Product product) {
        binding.setProduct(product);
    }

    /**
     * Update Text of Add Button and Remove
     *
     * @param openedFromCart Arguments received from fragment call this
     */
    @Override
    public void updateView(boolean openedFromCart) {
        binding.btAdd.setText(getString(openedFromCart ? R.string.add_to_wishlist : R.string.add_to_basket));
        binding.btRemove.setText(getString(openedFromCart ? R.string.remove_from_basket : R.string.remove_from_wishlist));
    }

    /**
     * Remove product is clicked by user
     * called when user click remove button
     * return screen call this to handle {@link CartFragment} or {@link FavoriteProductFragment}
     *
     * @param productId current product ID
     */
    @Override
    public void removeProduct(int productId) {
        if (caller instanceof CartFragment) {
            ((CartFragment) caller).onDeleteProduct(productId);
        } else if (caller instanceof FavoriteProductFragment) {
            ((FavoriteProductFragment) caller).removeProductFromWishList(productId);
        }
        this.dismiss();
    }

    @Override
    public void openAddToCartFragment(Product product) {
        AddProductToCartFragment addProductToCartFragment = new AddProductToCartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        addProductToCartFragment.setArguments(bundle);
        addProductToCartFragment.show(getParentFragmentManager(), AddProductToCartFragment.TAG);
        this.dismiss();
    }

    @Override
    public void dismissFragment() {
        this.dismiss();
    }

    /**
     * Remove references View when Fragment destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        caller = null;
    }
}
