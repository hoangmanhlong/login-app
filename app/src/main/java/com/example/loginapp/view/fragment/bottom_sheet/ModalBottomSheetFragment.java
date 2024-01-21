package com.example.loginapp.view.fragment.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.databinding.FragmentBottomSheetBinding;
import com.example.loginapp.presenter.BottomSheetPresenter;
import com.example.loginapp.view.fragment.product_detail.ProductFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheetFragment extends BottomSheetDialogFragment implements SheetView {

    private BottomSheetPresenter presenter;

    private int count = 1;

    private FragmentBottomSheetBinding binding;
    private int productId;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new BottomSheetPresenter(this);
        binding.setFragment(this);
        if (getArguments() != null) {
            productId = getArguments().getInt(ProductFragment.PRODUCT_KEY);
            presenter.getProduct(productId);
        }
    }

    public static final String TAG = "ModalBottomSheet";

    @Override
    public void onLoadProduct(Product product) {
        binding.setProduct(product);
    }

    public void onPlusBtnClick() {
        count++;
        binding.quantity.setText(String.valueOf(count));
    }

    public void onMinusBtnClick() {
        if (count > 1) count--;
        binding.quantity.setText(String.valueOf(count));
    }

    public void onBuyClick() {
        NavController navController = Navigation.findNavController(requireParentFragment().requireView());
        navController.navigate(R.id.action_productFragment_to_checkoutInfoFragment);
    }
}
