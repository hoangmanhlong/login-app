package com.example.loginapp.view.fragment.user_profile;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentUserProfileBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.UserProfilePresenter;
import com.example.loginapp.view.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class UserProfileFragment extends Fragment implements UserProfileView {

    private FragmentUserProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        UserProfilePresenter presenter = new UserProfilePresenter(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setProfileFragment(this);
        LinearLayout bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);;
    }

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout).setMessage(R.string.logout_message)
            .setPositiveButton(
                R.string.positive_button_title,
                (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finish();
                }
            )
            .setNegativeButton(R.string.negative_button_title, (dialog, which) -> {
            })
            .setCancelable(true)
            .show();
    }

    public void goEditUserScreen() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_userProfileFragment_to_editUserInformationFragment);
    }

    public void goVoucherScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userProfileFragment_to_voucherFragment);
    }

    @Override
    public void getUserData(UserData userData) {
        binding.setUserData(new UserData(userData.getUsername(), userData.getPhotoUrl()));
    }
    public void dialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor.

    }
}