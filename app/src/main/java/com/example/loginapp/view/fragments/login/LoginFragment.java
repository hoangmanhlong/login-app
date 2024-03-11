package com.example.loginapp.view.fragments.login;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentLoginBinding;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.state.LoginEmailButtonObserver;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment implements LoginView {

    private FragmentLoginBinding binding;

    private final LoginPresenter presenter = new LoginPresenter(this);

    private NavController navController;

    private Dialog dialog;

    private TextInputEditText[] editTexts;

    private Button loginWithEmailButton;

    private TextView loginEmailFailTextView;

    private Button btRequestOtp;

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
        loginWithEmailButton = binding.loginEmailBtn;
        loginEmailFailTextView = binding.tvLoginFailed;
        btRequestOtp = binding.btRequestOtp;
        etPhoneNumber = binding.etPhoneNumber;
        editTexts = new TextInputEditText[]{binding.emailTextInputEditText, binding.passwordTextInputEditText};
        loginWithEmailButton.setEnabled(!editTexts[0].getText().toString().trim().isEmpty() && !editTexts[1].getText().toString().trim().isEmpty());
        btRequestOtp.setEnabled(!etPhoneNumber.getText().toString().trim().isEmpty());
        loginWithEmailButtonObserver();
        phoneNumberTextViewListener();
//        getDataShared();
    }

    private void phoneNumberTextViewListener() {
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

//    private void getDataShared() {
//        if (getArguments() != null) {
//            String email = getArguments().getString(Constant.EMAIL_KEY);
//            String password = getArguments().getString(Constant.PASSWORD_KEY);
//            editTexts[0].setText(email);
//            editTexts[1].setText(password);
//            onEmailClick();
//        }
//    }


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
        String email = binding.emailTextInputEditText.getText().toString().trim();
        String password = binding.passwordTextInputEditText.getText().toString().trim();
        presenter.loginWithEmail(email, password, requireActivity());
    }

    public void goRegisterScreen() {
        navController.navigate(R.id.action_global_registerFragment);
    }

    private void loginWithEmailButtonObserver() {
        editTexts[0].addTextChangedListener(new LoginEmailButtonObserver(
                loginWithEmailButton,
                loginEmailFailTextView,
                editTexts
        ));

        editTexts[1].addTextChangedListener(new LoginEmailButtonObserver(
                loginWithEmailButton,
                loginEmailFailTextView,
                editTexts
        ));
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
            navController.popBackStack();
            navController.navigate(R.id.firstFragment);
        } else {
            binding.tvLoginFailed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void requestOTPButtonEnabled(boolean enabled) {
        btRequestOtp.setEnabled(enabled);
    }

    public void onLoginPhoneNumberClick() {
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PHONE_NUMBER_KEY, phoneNumber);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_verificationFragment, bundle);
    }
}
