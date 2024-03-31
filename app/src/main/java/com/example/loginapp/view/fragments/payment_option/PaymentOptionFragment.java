package com.example.loginapp.view.fragments.payment_option;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentPaymentOptionBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.presenter.PaymentOptionPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.fragments.select_voucher_fragment.MessageVoucherSelected;

import org.greenrobot.eventbus.EventBus;

public class PaymentOptionFragment extends Fragment implements PaymentOptionView {

    private PaymentOptionPresenter presenter;

    private FragmentPaymentOptionBinding binding;

    private NavController navController;
    
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PaymentOptionPresenter(this);
        navController = NavHostFragment.findNavController(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentOptionBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        getSharedData();
        onFpxClick();
    }

    private void getSharedData() {
        if (getArguments() != null) {
            Order order = (Order) getArguments().getSerializable(Constant.ORDER_KEY);
            boolean isSaveDeliveryAddress = getArguments().getBoolean(Constant.SAVE_ADDRESS_KEY);
            boolean isProductsFromShoppingCart = getArguments().getBoolean(Constant.IS_PRODUCTS_FROM_CART);
            if (order != null) presenter.setOrder(order);
            presenter.setProductsFromShoppingCart(isProductsFromShoppingCart);
            presenter.setSaveDeliveryAddress(isSaveDeliveryAddress);
        }
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void onFpxClick() {
        presenter.setPaymentMethod(PaymentMethod.FPX);
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
    }

    public void onVisaClick() {
        presenter.setPaymentMethod(PaymentMethod.CreditDebitCard);
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
    }

    public void onPaypalClick() {
        presenter.setPaymentMethod(PaymentMethod.PayPal);
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_selected);
    }

    public void onPayButtonClick() {
        presenter.createOrder();
    }

    @Override
    public void goOrderSuccessScreen() {
        MessageVoucherSelected messageVoucherSelected =
                EventBus.getDefault().getStickyEvent(MessageVoucherSelected.class);
        if (messageVoucherSelected != null)
            EventBus.getDefault().removeStickyEvent(messageVoucherSelected);
        navController.navigate(R.id.action_paymentOptionFragment_to_orderSuccessFragment);
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
        presenter.DetachView();
        presenter = null;
        navController = null;
    }

    @Override
    public void isLoading(Boolean loading) {
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void hasVoucher(boolean hasVoucher) {
        binding.setHasVoucher(hasVoucher);
    }

    @Override
    public void setView(String merchandiseSubtotal, String shippingCost,String reducedPrice, String totalPayment) {
        binding.setMerchandiseSubtotal(merchandiseSubtotal);
        binding.setShippingCost(shippingCost);
        binding.setReducedPrice(reducedPrice);
        binding.setTotalPayment(totalPayment);
    }
}