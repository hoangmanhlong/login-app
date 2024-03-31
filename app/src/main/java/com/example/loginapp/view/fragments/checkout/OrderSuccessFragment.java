package com.example.loginapp.view.fragments.checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.databinding.FragmentOrderSuccessBinding;

public class OrderSuccessFragment extends Fragment {

    private FragmentOrderSuccessBinding binding;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        if (getActivity() != null) {
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    onBackHomeScreen();
                }
            });
        }

        binding.btBack.setOnClickListener(v -> onBackHomeScreen());
    }

    private void onBackHomeScreen() {
        navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        navController = null;
    }
}