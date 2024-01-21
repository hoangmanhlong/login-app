package com.example.loginapp.view.fragment.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentLoginBinding;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.view.HideKeyboard;
import com.example.loginapp.view.LoadingDialog;
import com.example.loginapp.view.activities.MainActivity;
import com.example.loginapp.view.state.LoginEmailButtonObserver;
import com.example.loginapp.view.state.LoginPhoneNumberButtonObserver;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragment extends Fragment implements LoginView {

    public static String PHONE_NUMBER_KEY = "PNK";

    private FragmentLoginBinding binding;

    private final LoginPresenter presenter = new LoginPresenter(this);

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLoginFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());

        if (getArguments() != null) {
            String email = getArguments().getString("email");
            String password = getArguments().getString("password");
            if (!email.isEmpty() && !password.isEmpty()) {
                binding.emailInput.setText(email);
                binding.passwordInput.setText(password);
                binding.loginEmailBtn.setEnabled(true);
                onEmailClick();
            }
        }

        if (!binding.etPhoneNumber.getText().toString().isEmpty()) binding.requestOtpBtn.setEnabled(true);

        binding.loginEmailBtn.setEnabled(
            !Objects.requireNonNull(binding.emailInput.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(binding.passwordInput.getText()).toString().isEmpty());

        HideKeyboard.setupHideKeyboard(view, requireActivity());

        binding.emailInput.addTextChangedListener(new LoginEmailButtonObserver(
            binding.loginEmailBtn,
            binding.tvLoginFailed,
            binding.emailInput,
            binding.passwordInput
        ));

        binding.passwordInput.addTextChangedListener(new LoginEmailButtonObserver(
            binding.loginEmailBtn,
            binding.tvLoginFailed,
            binding.emailInput,
            binding.passwordInput
        ));

        binding.etPhoneNumber.addTextChangedListener(new LoginPhoneNumberButtonObserver(binding.requestOtpBtn));
    }

    public void onNumberPhoneBtn() {
        binding.numberPhoneInput.setVisibility(View.VISIBLE);
        binding.linearInputEmail.setVisibility(View.GONE);
        binding.loginPhoneNumber.setBackgroundResource(R.drawable.background_login_select);
        binding.loginEmail.setBackgroundResource(R.drawable.wrap_select_option_login_background);
    }

    public void onEmailClick() {
        binding.numberPhoneInput.setVisibility(View.GONE);
        binding.linearInputEmail.setVisibility(View.VISIBLE);
        binding.loginPhoneNumber.setBackgroundResource(R.drawable.wrap_select_option_login_background);
        binding.loginEmail.setBackgroundResource(R.drawable.background_login_select);
    }

    public void onLogin() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        presenter.loginWithEmail(email, password, requireActivity());
    }

    public void goRegisterScreen() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_loginFragment_to_registerFragment);
    }

    public void goHomeScreen() {
        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onLoginMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowProcessBar(Boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void isLoginFailed(boolean check) {
        if (check) binding.tvLoginFailed.setVisibility(View.VISIBLE);
        else binding.tvLoginFailed.setVisibility(View.GONE);
    }

    public void onLoginPhoneNumberClick() {
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_NUMBER_KEY, phoneNumber);
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_loginFragment_to_verificationFragment, bundle);
    }
}
