package com.example.loginapp.view.fragments.user_profile;


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
import com.example.loginapp.databinding.FragmentUserProfileDetailBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.UserProfileDetailPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppConfirmDialog;
import com.google.firebase.auth.FirebaseAuth;


public class UserProfileDetailFragment extends Fragment implements UserProfileDetailView {

    private static final String TAG = UserProfileDetailFragment.class.getSimpleName();

    private UserProfileDetailPresenter presenter;

    private FragmentUserProfileDetailBinding binding;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(requireParentFragment());
        presenter = new UserProfileDetailPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileDetailBinding.inflate(inflater, container, false);
        binding.setProfileFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initData();
    }

    public void onLogoutButtonClick() {
        AppConfirmDialog.show(
                requireContext(),
                getString(R.string.logout),
                getString(R.string.logout_message),
                new AppConfirmDialog.AppConfirmDialogButtonListener() {
                    @Override
                    public void onPositiveButtonClickListener() {
                        navController.popBackStack(navController.getCurrentDestination().getId(), true);
                        navController.navigate(R.id.overviewFragment);
                        FirebaseAuth.getInstance().signOut();
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                }
        );
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
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addUserDataValueEventListener();
        presenter.addOrdersValueEventListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
        navController = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeUserDataValueEventListener();
        presenter.removeOrdersValueEventListener();
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
        bundle.putSerializable(Constant.USER_KEY_NAME, presenter.getUserData());
        navController.navigate(R.id.editUserInformationFragment, bundle);
    }

    public void onChangeLanguageClick() {
        navController.navigate(R.id.selectLanguageFragment);
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