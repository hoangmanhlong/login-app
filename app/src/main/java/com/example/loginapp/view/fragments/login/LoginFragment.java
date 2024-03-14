package com.example.loginapp.view.fragments.login;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentLoginBinding;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment implements LoginView {

    private FragmentLoginBinding binding;

    private final LoginPresenter presenter = new LoginPresenter(this);

    private NavController navController;

    private Dialog dialog;

    private TextInputEditText[] editTexts;

    private EditText etPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        binding.setLoginFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        etPhoneNumber = binding.etPhoneNumber;
        editTexts = new TextInputEditText[]{binding.emailTextInputEditText, binding.passwordTextInputEditText};
        presenter.initData();
        loginWithEmailButtonObserver();
        phoneNumberEditTextListener();
    }

    private void phoneNumberEditTextListener() {
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPhoneNumber(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void onLoginWithEmailClick() {
        presenter.loginWithEmail();
    }

    public void goRegisterScreen() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.toString(), "onDestroy: ");
        binding = null;
        System.gc();
    }

    private void loginWithEmailButtonObserver() {
        editTexts[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setEmail(s.toString().trim());
                binding.setLoginEmailAndPasswordMessageVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTexts[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPassword(s.toString().trim());
                binding.setLoginEmailAndPasswordMessageVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void isLoading(@NonNull Boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void isLoginSuccess(boolean isSuccess) {
        dialog.dismiss();
        if (isSuccess) {
            int startDestinationId = navController.getGraph().getStartDestinationId();
            if (navController.getPreviousBackStackEntry() != null) {
                navController.popBackStack(startDestinationId, true);
                navController.navigate(startDestinationId);
            } else {
                navController.popBackStack(R.id.loginFragment, true);
                navController.navigate(startDestinationId);
            }

        } else {
            binding.setLoginEmailAndPasswordMessageVisible(true);
        }
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void requestOTPButtonEnabled(boolean enabled) {
        binding.setLoginWithPhoneNumberButtonEnabled(enabled);
    }

    @Override
    public void loginEmailAndPasswordButtonEnabled(boolean visible) {
        binding.setLoginEmailAndPasswordButtonEnable(visible);
    }

    public void onLoginPhoneNumberClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PHONE_NUMBER_KEY, presenter.getPhoneNumber());
        navController.navigate(R.id.action_loginFragment_to_verificationFragment, bundle);
    }
}
