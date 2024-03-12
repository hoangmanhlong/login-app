package com.example.loginapp.view.fragments.cart;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.adapter.SwipeHelper;
import com.example.loginapp.adapter.cart_adapter.CartAdapter;
import com.example.loginapp.adapter.cart_adapter.CartItemClickListener;
import com.example.loginapp.databinding.FragmentCartBinding;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.CartPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.fragments.select_voucher_fragment.MessageVoucherSelected;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CartFragment extends Fragment implements CartView, CartItemClickListener {

    private NavController navController;

    private final String TAG = getClass().getSimpleName();

    private final CartPresenter presenter = new CartPresenter(this);

    private FragmentCartBinding binding;

    private final CartAdapter adapter = new CartAdapter(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        initView();
        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        MessageVoucherSelected messageVoucherSelected =
                EventBus.getDefault().getStickyEvent(MessageVoucherSelected.class);
        if (messageVoucherSelected != null)
            EventBus.getDefault().removeStickyEvent(messageVoucherSelected);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getSelectedVoucher(MessageVoucherSelected messageVoucherSelected) {
        presenter.setSelectedVoucher(messageVoucherSelected.getVoucher());
    }

    @Override
    public void bindDiscountCode(String code) {
        binding.discountCode.setText(code);
    }

    private void initView() {
        binding.setFragment(this);
        binding.basketRecyclerView.setAdapter(adapter);
        presenter.initBasket();
        new SwipeHelper(requireContext(), binding.basketRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        adapter::getProductByPosition
                ));
            }
        };
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void bindProducts(List<FirebaseProduct> products) {
        adapter.submitList(products);
        adapter.notifyItemInserted(products.size() - 1);
    }

    @Override
    public void notifyItemRemoved(int index) {
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void notifyItemChanged(int index) {
        adapter.notifyItemChanged(index);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        navController.navigate(R.id.action_global_productFragment, bundle);
    }

    @Override
    public void updateQuantity(int id, int quantity) {
        presenter.updateQuantity(id, quantity);
    }

    @Override
    public void onItemChecked(FirebaseProduct product, boolean checked) {
        presenter.onItemChecked(product, checked);
    }

    @Override
    public void setCheckAllChecked(boolean isChecked) {
        binding.selectAllCheckbox.setChecked(isChecked);
    }

    @Override
    public void showCheckoutView(Boolean check) {
        if ((binding.checkoutView.getVisibility() == View.GONE) && check)
            AppAnimationState.setCheckoutViewState(binding.checkoutView, true);
        if ((binding.checkoutView.getVisibility() == View.VISIBLE) && !check)
            AppAnimationState.setCheckoutViewState(binding.checkoutView, false);
    }

    @Override
    public void showCheckAllCheckbox(Boolean visible) {
        if (visible) binding.checkAllView.setVisibility(View.VISIBLE);
        else binding.checkAllView.setVisibility(View.GONE);
    }

    @Override
    public void isBasketEmpty(Boolean isEmpty) {
        binding.setIsBasketEmpty(isEmpty);
    }

    @Override
    public void isCheckAllCheckboxChecked(Boolean checked) {
        binding.selectAllCheckbox.setChecked(checked);
    }

    public void onCheckboxAllClick() {
        presenter.updateCheckboxAllSelected(binding.selectAllCheckbox.isChecked());
    }

    @Override
    public void onDeleteProduct(FirebaseProduct product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> presenter.deleteProductInFirebase(product))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void setTotal(String subtotal, String quantity, String total) {
        binding.setQuantity(quantity);
        binding.setPrice(subtotal);
        binding.setTotal(total);
    }

    public void onCheckoutClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, presenter.getOrder());
        bundle.putBoolean(Constant.IS_PRODUCTS_FROM_CART, true);
        navController.navigate(R.id.checkoutInfoFragment, bundle);
    }

    public void onSelectCodeClick() {
        navController.navigate(R.id.selectVoucherFragment);
    }

    public void clearDiscountCode() {
        presenter.setClearCode(true);
    }

    @Override
    public void clearDiscountCode(Boolean clear) {
        binding.clearDiscountCodeView.setVisibility(clear ? View.GONE : View.VISIBLE);
        binding.selectDiscountCodeView.setVisibility(clear ? View.VISIBLE : View.GONE);
    }
}