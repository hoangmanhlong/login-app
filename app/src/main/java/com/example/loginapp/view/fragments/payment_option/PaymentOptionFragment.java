package com.example.loginapp.view.fragments.payment_option;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.databinding.FragmentPaymentOptionBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.presenter.PaymentOptionPresenter;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.LoadingDialog;

public class PaymentOptionFragment extends Fragment implements PaymentOptionView {

    private final PaymentOptionPresenter presenter = new PaymentOptionPresenter(this);

    private FragmentPaymentOptionBinding binding;
    
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentOptionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        onFpxClick();
        presenter.getOrder((Order) getArguments().getSerializable(Constant.ORDER_KEY));
        presenter.updateSaveDeliveryAddressState(getArguments().getBoolean(Constant.SAVE_ADDRESS_KEY));
        presenter.setCart(getArguments().getBoolean(Constant.IS_CART));
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
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
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_paymentOptionFragment_to_orderSuccessFragment);
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
    public void setView(String subTotal, String sippingCost, String total) {
        binding.setSubtotal(subTotal);
        binding.setShippingCosts(sippingCost);
        binding.setTotalPayment(total);
    }
}