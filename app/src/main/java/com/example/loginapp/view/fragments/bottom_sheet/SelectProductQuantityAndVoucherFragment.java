package com.example.loginapp.view.fragments.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentSelectProductQuantityAndVoucherBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.SelectProductQuantityAndVoucherPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.select_voucher_fragment.MessageVoucherSelected;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SelectProductQuantityAndVoucherFragment
        extends BottomSheetDialogFragment
        implements SelectProductQuantityAndVoucherView {

    public static final String TAG = SelectProductQuantityAndVoucherFragment.class.getSimpleName();

    private SelectProductQuantityAndVoucherPresenter presenter;

    private FragmentSelectProductQuantityAndVoucherBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SelectProductQuantityAndVoucherPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectProductQuantityAndVoucherBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSharedData();
    }

    private void getSharedData() {
        if (getArguments() != null) {
            Product product = (Product) getArguments().getSerializable(Constant.PRODUCT_KEY);
            if (product != null) presenter.setProduct(product);
        }
    }

    public void onPlusBtnClick() {
        presenter.onPlusBtnClick();
    }

    public void onMinusBtnClick() {
        presenter.onMinusBtnClick();
    }

    public void onCheckoutButtonClick() {
        presenter.onCheckoutButtonClick();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getSelectedVoucher(MessageVoucherSelected messageVoucherSelected) {
        presenter.setVoucher(messageVoucherSelected.getVoucher());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
    }

    public void onClearDiscountCodeClick() {
        presenter.clearVoucher();
        MessageVoucherSelected stickyEvent = EventBus.getDefault().getStickyEvent(MessageVoucherSelected.class);
        if (stickyEvent != null) EventBus.getDefault().removeStickyEvent(stickyEvent);
    }

    public void onSelectCodeClick() {
        NavHostFragment.findNavController(this).navigate(R.id.selectVoucherFragment);
    }

    @Override
    public void navigateToCheckoutInfoFragment(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, order);
        bundle.putBoolean(Constant.IS_PRODUCTS_FROM_CART, false);
        Navigation.findNavController(requireParentFragment().requireView())
                .navigate(R.id.action_productFragment_to_checkoutInfoFragment, bundle);
    }

    @Override
    public void isSelectVoucherViewVisible(boolean visible) {
        binding.setSelectVoucherVisible(visible);
    }

    @Override
    public void bindQuantityAndTotal(String quantity, String total) {
        binding.quantity.setText(quantity);
        binding.setTotal(total);
    }

    @Override
    public void bindProduct(Product product) {
        binding.setProduct(product);
    }

    @Override
    public void bindDiscountCode(String discountCode) {
        binding.tvDiscountCode.setText(discountCode);
    }
}
