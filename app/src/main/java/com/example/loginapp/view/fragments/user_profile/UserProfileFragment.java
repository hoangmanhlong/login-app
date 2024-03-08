package com.example.loginapp.view.fragments.user_profile;


import static android.content.Intent.getIntent;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentUserProfileBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.UserProfilePresenter;
import com.example.loginapp.utils.Constant;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class UserProfileFragment extends Fragment implements UserProfileView {

    private UserProfilePresenter presenter = new UserProfilePresenter(this);

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
        new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.positive_button_title, (dialog, which) -> FirebaseAuth.getInstance().signOut())
                .setNegativeButton(R.string.negative_button_title, null)
                .show();
    }

    @Override
    public void bindUserData(UserData userData) {
        binding.setUserData(userData);
    }

    @Override
    public void bindNumberOfOrders(int numberOfProcessingOrder, int numberOfCompletedOrder, int numberOfCancelOrder, int numberOfReturnOrder) {
        binding.tvNumberOfOrdersProcessing.setText(String.valueOf(numberOfProcessingOrder));
        binding.tvNumberOfOrdersCompleted.setText(String.valueOf(numberOfCompletedOrder));
        binding.tvNumberOfOrdersCancel.setText(String.valueOf(numberOfCancelOrder));
        binding.tvNumberOfOrdersReturn.setText(String.valueOf(numberOfReturnOrder));
    }

    public void onImageAvatarClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER_KEY_NAME, presenter.getCurrentUser());
        NavHostFragment.findNavController(this).navigate(R.id.editUserInformationFragment, bundle);
    }

    public void onChangeLanguageClick() {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("vi");
// Call this on the main thread as it may require Activity.restart()
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    public void goVoucherScreen() {
        NavHostFragment.findNavController(this).navigate(R.id.action_global_voucherFragment);
    }

    public void goOrdersScreen() {
        NavHostFragment.findNavController(this).navigate(R.id.ordersFragment);
    }

    public void goShippingAddressesScreen() {
        NavHostFragment.findNavController(this).navigate(R.id.deliveryAddressFragment);
    }

    public void goCoinRewardScreen() {
        NavHostFragment.findNavController(this).navigate(R.id.coinsRewardFragment);
    }
}