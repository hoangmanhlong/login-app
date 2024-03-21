package com.example.loginapp.view.fragments.select_payment_method;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentSelectPaymentMethodBinding;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.utils.Constant;


public class SelectPaymentMethodFragment extends Fragment {

    private NavController navController;

    private FragmentSelectPaymentMethodBinding binding;

    private PaymentMethod selectedPaymentMethod;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectPaymentMethodBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        binding.setFragment(this);
        if (getArguments() != null) {
            selectedPaymentMethod = (PaymentMethod) getArguments().getSerializable(Constant.PAYMENT_METHOD_KEY);
            if (selectedPaymentMethod != null) {
                switch (selectedPaymentMethod) {
                    case FPX:
                        onFpxClick();
                        break;
                    case CreditDebitCard:
                        onVisaClick();
                        break;
                    case PayPal:
                        onPaypalClick();
                        break;
                }
            }
        }
    }

    public void onFpxClick() {
        selectedPaymentMethod = PaymentMethod.FPX;
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.btOK.setVisibility(View.VISIBLE);
    }

    public void onVisaClick() {
        selectedPaymentMethod = PaymentMethod.CreditDebitCard;
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.btOK.setVisibility(View.VISIBLE);
    }

    public void onPaypalClick() {
        selectedPaymentMethod = PaymentMethod.PayPal;
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.btOK.setVisibility(View.VISIBLE);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void onOkButtonClick() {
        navController.getPreviousBackStackEntry()
                .getSavedStateHandle().set(Constant.PAYMENT_METHOD_KEY, selectedPaymentMethod);
        navController.navigateUp();
    }
}