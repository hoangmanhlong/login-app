package com.example.loginapp.view.fragments.user_profile;


import android.app.AlertDialog;
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
import com.example.loginapp.databinding.FragmentUserProfileBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.UserProfilePresenter;
import com.google.firebase.auth.FirebaseAuth;


public class UserProfileFragment extends Fragment implements UserProfileView {

    UserProfilePresenter presenter = new UserProfilePresenter(this);

    private FragmentUserProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setProfileFragment(this);
        presenter.initData();
    }

    public void logOut() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout).setMessage(R.string.logout_message)
                .setPositiveButton(R.string.positive_button_title, (dialog, which) -> FirebaseAuth.getInstance().signOut())
                .setNegativeButton(R.string.negative_button_title, null)
                .setCancelable(true)
                .show();
    }

    public void goEditUserScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER_KEY_NAME, presenter.getCurrentUser());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userProfileFragment_to_editUserInformationFragment, bundle);
    }

    public void goVoucherScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_voucherFragment);
    }

    public void goOrdersScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userProfileFragment_to_ordersFragment);
    }

    @Override
    public void bindUserData(UserData userData) {
        binding.setUserData(userData);
    }

    public void goShippingAddressesScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userProfileFragment_to_deliveryAddressFragment);
    }

    public void goCoinRewardScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userProfileFragment_to_coinsRewardFragment);
    }
}