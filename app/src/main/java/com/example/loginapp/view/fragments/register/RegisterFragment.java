package com.example.loginapp.view.fragments.register;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.databinding.FragmentRegisterBinding;
import com.example.loginapp.presenter.RegisterPresenter;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.state.RegisterButtonObserver;

public class RegisterFragment extends Fragment implements RegisterView {

    private FragmentRegisterBinding binding;

    private final RegisterPresenter registerPresenter = new RegisterPresenter(this);

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setRegisterFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        buttonState();
    }

    public void onRegisterButtonClick() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordInput.getText().toString().trim();
        registerPresenter.register(email, password, confirmPassword, requireActivity());
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void isLoading(Boolean loading) {
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    public void onNavigate() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }


    public void buttonState() {
        binding.emailInput.addTextChangedListener(new RegisterButtonObserver(binding.registerButton,
                binding.emailInput, binding.passwordInput, binding.confirmPasswordInput
        ));
        binding.passwordInput.addTextChangedListener(new RegisterButtonObserver(binding.registerButton,
                binding.emailInput, binding.passwordInput, binding.confirmPasswordInput
        ));
        binding.confirmPasswordInput.addTextChangedListener(new RegisterButtonObserver(binding.registerButton,
                binding.emailInput, binding.passwordInput, binding.confirmPasswordInput
        ));
    }
}
