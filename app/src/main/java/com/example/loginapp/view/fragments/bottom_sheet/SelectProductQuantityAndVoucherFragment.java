package com.example.loginapp.view.fragments.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentSelectProductQuantityAndVoucherBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.select_voucher_fragment.MessageVoucherSelected;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

public class SelectProductQuantityAndVoucherFragment extends BottomSheetDialogFragment implements SheetView {

    public static final String TAG = SelectProductQuantityAndVoucherFragment.class.getSimpleName();

    private int quantity = 1;

    private Product currentProduct;

    private Order order = new Order();

    private FragmentSelectProductQuantityAndVoucherBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectProductQuantityAndVoucherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        binding.setSelectVoucherVisible(true);
        getSharedData();
        binding.setProduct(currentProduct);
        updateUI();
    }

    private void getSharedData() {
        if (getArguments() != null) {
            Product product = (Product) getArguments().getSerializable(Constant.PRODUCT_KEY);
            if (product != null) currentProduct = product;
        }
    }

    public void onPlusBtnClick() {
        quantity++;
        updateUI();
    }

    public void onMinusBtnClick() {
        if (quantity > 1) quantity--;
        updateUI();
    }

    private void updateUI() {
        double total = currentProduct.getPrice() * quantity;
        Voucher voucher = order.getVoucher();
        if (voucher != null)
            if (voucher.getVoucherType() == VoucherType.Discount)
                total = (total - ((total * voucher.getDiscountPercentage()) / 100));
        total = Math.round(total * 100.00) / 100.00;
        binding.quantity.setText(String.valueOf(quantity));
        binding.setTotal(String.valueOf(total));
    }

    public void onBuyClick() {
        Bundle bundle = new Bundle();
        order.setOrderProducts(Collections.singletonList(currentProduct.toOrderProduct(quantity)));
        bundle.putSerializable(Constant.ORDER_KEY, order);
        bundle.putBoolean(Constant.IS_PRODUCTS_FROM_CART, false);
        Navigation.findNavController(requireParentFragment().requireView())
                .navigate(R.id.action_productFragment_to_checkoutInfoFragment, bundle);
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
        Voucher voucher1 = messageVoucherSelected.getVoucher();
        order.setVoucher(voucher1);
        binding.setSelectVoucherVisible(false);
        binding.discountCode.setText(voucher1.getVoucherCode());
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public void onClearDiscountCodeClick() {
        binding.setSelectVoucherVisible(true);
        order.setVoucher(null);
        updateUI();
        MessageVoucherSelected stickyEvent = EventBus.getDefault().getStickyEvent(MessageVoucherSelected.class);
        if (stickyEvent != null) EventBus.getDefault().removeStickyEvent(stickyEvent);
    }

    public void onSelectCodeClick() {
        Navigation.findNavController(requireParentFragment().requireView()).navigate(R.id.action_productFragment_to_selectVoucherFragment);
    }
}
