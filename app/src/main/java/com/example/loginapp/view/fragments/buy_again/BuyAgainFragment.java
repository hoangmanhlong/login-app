package com.example.loginapp.view.fragments.buy_again;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.adapter.order_product_adapter.OrderProductAdapter;
import com.example.loginapp.databinding.FragmentBuyAgainBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.BuyAgainPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppConfirmDialog;
import com.example.loginapp.view.commonUI.LoadingDialog;

import java.util.List;


public class BuyAgainFragment extends Fragment implements BuyAgainView {

    private BuyAgainPresenter presenter;

    private FragmentBuyAgainBinding binding;

    private NavController navController;

    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new BuyAgainPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBuyAgainBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        presenter.initData();

        NavBackStackEntry navBackStackEntry = navController.getCurrentBackStackEntry();

        if (navBackStackEntry != null) {
            LiveData<DeliveryAddress> deliveryAddressLiveData
                    = navBackStackEntry.getSavedStateHandle().getLiveData(Constant.DELIVERY_ADDRESS_KEY);
            LiveData<Voucher> voucherLiveData
                    = navBackStackEntry.getSavedStateHandle().getLiveData(Constant.VOUCHER_KEY_NAME);
            LiveData<PaymentMethod> paymentMethodLiveData
                    = navBackStackEntry.getSavedStateHandle().getLiveData(Constant.PAYMENT_METHOD_KEY);

            deliveryAddressLiveData.observe(getViewLifecycleOwner(), presenter::setDeliveryAddress);
            voucherLiveData.observe(getViewLifecycleOwner(), presenter::setVoucher);
            paymentMethodLiveData.observe(getViewLifecycleOwner(), presenter::setPaymentMethod);
        }
    }

    @Override
    public void getSharedData() {
        if (getArguments() != null) {
            Order order = (Order) getArguments().getSerializable(Constant.ORDER_KEY);
            if (order != null) presenter.setCurrentOrder(order);
        }
    }

    @Override
    public void bindPaymentMethod(PaymentMethod paymentMethod) {
        binding.setPaymentMethod(paymentMethod);
    }

    @Override
    public void bindMerchandiseSubtotal(String merchandiseSubtotal) {
        binding.setMerchandiseSubtotal(merchandiseSubtotal);
    }

    @Override
    public void bindShippingCost(String shippingCost) {
        binding.setShippingCost(shippingCost);
    }

    @Override
    public void bindVoucherCode(String voucherCode) {
        binding.setVoucherCode(voucherCode);
    }

    @Override
    public void hasVoucher(boolean hasVoucher) {
        binding.setHasVoucher(hasVoucher);
    }

    @Override
    public void bindReducedPrice(String reducedPrice) {
        binding.setReducedPrice(reducedPrice);
    }

    @Override
    public void bindTotalPayment(String totalPayment) {
        binding.setTotalPayment(totalPayment);
    }

    @Override
    public void onCheckoutSuccess(boolean success) {
        isLoading(false);
        if (success) navController.navigate(R.id.action_buyAgainFragment_to_orderSuccessFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        dialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
        navController = null;
    }

    @Override
    public void isLoading(boolean isLoading) {
        if (isLoading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void navigateSelectPaymentMethodFragment(PaymentMethod paymentMethod) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PAYMENT_METHOD_KEY, paymentMethod);
        navController.navigate(R.id.action_buyAgainFragment_to_selectPaymentMethodFragment, bundle);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void onSelectVoucherViewClick() {
        navController.navigate(R.id.action_buyAgainFragment_to_selectVoucherFragment);
    }

    public void onPaymentMethodClick() {
        presenter.onPaymentMethodClick();
    }

    public void onCheckoutButtonClick() {
        AppConfirmDialog.show(
                requireContext(),
                getString(R.string.checkout),
                getString(R.string.checkout_confirm),
                new AppConfirmDialog.AppConfirmDialogButtonListener() {
                    @Override
                    public void onPositiveButtonClickListener() {
                        presenter.checkout();
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                }

        );
    }

    public void onDeliveryAddressClick() {
        navController.navigate(R.id.action_buyAgainFragment_to_selectDeliveryAddressFragment);
    }

    @Override
    public void bindAddress(DeliveryAddress address) {
        binding.setDeliveryAddress(address);
    }

    @Override
    public void bindOrderProducts(List<OrderProduct> products) {
        OrderProductAdapter adapter = new OrderProductAdapter(products);
        binding.orderProductRecyclerview.setAdapter(adapter);
    }
}