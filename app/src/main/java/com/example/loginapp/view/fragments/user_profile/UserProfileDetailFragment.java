package com.example.loginapp.view.fragments.user_profile;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentUserProfileDetailBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.UserProfileDetailPresenter;
import com.example.loginapp.utils.Constant;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;


public class UserProfileDetailFragment extends Fragment implements UserProfileDetailView {

    private static final String TAG = UserProfileDetailFragment.class.getSimpleName();

    private final UserProfileDetailPresenter presenter = new UserProfileDetailPresenter(this);

    private FragmentUserProfileDetailBinding binding;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        binding.setProfileFragment(this);
        presenter.initData();
    }

    public void onLogoutButtonClick() {
        new MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    navController.popBackStack(navController.getCurrentDestination().getId(), true);
                    navController.navigate(R.id.overviewFragment);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void bindLanguageState(boolean isVietnamese) {
        binding.setIsVietnamese(isVietnamese);
    }

    @Override
    public void bindUserData(UserData userData) {
        binding.setUserData(userData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    @Override
    public void bindNumberOfOrders(
            int numberOfProcessingOrder,
            int numberOfCompletedOrder,
            int numberOfCancelOrder,
            int numberOfReturnOrder
    ) {
        binding.tvNumberOfOrdersProcessing.setText(String.valueOf(numberOfProcessingOrder));
        binding.tvNumberOfOrdersCompleted.setText(String.valueOf(numberOfCompletedOrder));
        binding.tvNumberOfOrdersCancel.setText(String.valueOf(numberOfCancelOrder));
        binding.tvNumberOfOrdersReturn.setText(String.valueOf(numberOfReturnOrder));
    }

    public void onImageAvatarClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER_KEY_NAME, presenter.getCurrentUser());
        navController.navigate(R.id.editUserInformationFragment, bundle);
    }

    public void onChangeLanguageClick() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_VIETNAMESE_LANGUAGE, presenter.isVietnamese());
        navController.navigate(R.id.selectLanguageFragment, bundle);
    }

    public void goVoucherScreen() {
        navController.navigate(R.id.action_global_voucherFragment);
    }

    public void goOrdersScreen() {
        navController.navigate(R.id.ordersFragment);
    }

    public void goShippingAddressesScreen() {
        navController.navigate(R.id.deliveryAddressFragment);
    }

    public void goCoinRewardScreen() {
        navController.navigate(R.id.coinsRewardFragment);
    }
}