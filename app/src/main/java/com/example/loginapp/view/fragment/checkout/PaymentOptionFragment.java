package com.example.loginapp.view.fragment.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentPaymentOptionBinding;

public class PaymentOptionFragment extends Fragment {
    private FragmentPaymentOptionBinding binding;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentPaymentOptionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        LinearLayout bottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void onFpxClick() {
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
    }

    public void onVisaClick() {
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
    }

    public void onPaypalClick() {
        binding.fpxBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.onlineBankingRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.visaBackground.setBackgroundResource(R.drawable.payment_card_no_selected_background);
        binding.visaRadio.setBackgroundResource(R.drawable.ic_radio_no_selected);
        binding.paypalBackground.setBackgroundResource(R.drawable.payment_card_selected_background);
        binding.payPalRadio.setBackgroundResource(R.drawable.ic_radio_selected);
    }

    public void goOrderSuccessScreen() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_paymentOptionFragment_to_orderSuccessFragment);
    }
}