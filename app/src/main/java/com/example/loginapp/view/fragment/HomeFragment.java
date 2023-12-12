package com.example.loginapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentHomeBinding;
import com.example.loginapp.presenter.HomePresenter;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomePresenter homePresenter;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @androidx.annotation.Nullable ViewGroup container,
        @androidx.annotation.Nullable Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @androidx.annotation.Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHomeFragment(this);
        homePresenter = new HomePresenter(this);
    }

    public void getLogout() {
        homePresenter.logout();
    }

    public void goLogoutScreen() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_homeFragment_to_overviewFragment);
    }
}