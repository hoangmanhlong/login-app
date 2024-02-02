package com.example.loginapp.view.fragment.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentOrderSuccessBinding;

public class OrderSuccessFragment extends Fragment {

    private FragmentOrderSuccessBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_orderSuccessFragment_to_homeFragment);
    }

    public void onBackHomeScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_orderSuccessFragment_to_homeFragment);
    }
}