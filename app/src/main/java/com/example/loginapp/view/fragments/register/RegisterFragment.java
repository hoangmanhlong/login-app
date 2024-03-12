package com.example.loginapp.view.fragments.register;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentRegisterBinding;
import com.example.loginapp.presenter.RegisterPresenter;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment implements RegisterView {

    private final String TAG = this.toString();
    private FragmentRegisterBinding binding;

    private final RegisterPresenter presenter = new RegisterPresenter(this);

    private TextInputLayout[] textInputLayouts;

    private TextInputEditText[] textInputEditTexts;

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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        textInputLayouts = new TextInputLayout[]{binding.emailTextInputLayout, binding.passwordTextInputLayout, binding.confirmTextInputLayout};
        textInputEditTexts = new TextInputEditText[]{binding.etEmail, binding.etPassword, binding.etConfirmPassword};
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        buttonState();
    }

    public void onRegisterButtonClick() {
        presenter.register();
    }

    @Override
    public void onMessage(int message) {
        binding.setIsSignUpStateMessageVisible(true);
        binding.setMessage(getString(message));
    }

    @Override
    public void isLoading(Boolean loading) {
        binding.btRegister.setText(loading ? R.string.loading___ : R.string.register);
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void isValidEmail(boolean isValid) {
        if (isValid) {
            textInputLayouts[0].setErrorEnabled(false);
        } else {
            textInputLayouts[0].setErrorEnabled(true);
            textInputLayouts[0].setError("Invalid email");
        }
    }

    @Override
    public void isValidPassword(boolean isValid) {
        if (isValid) {
            textInputLayouts[1].setErrorEnabled(false);
        } else {
            textInputLayouts[1].setErrorEnabled(true);
            textInputLayouts[1].setError("Invalid password");
        }
    }

    @Override
    public void isValidConfirmPassword(boolean isValid) {
        if (isValid) {
            textInputLayouts[2].setErrorEnabled(false);
        } else {
            textInputLayouts[2].setErrorEnabled(true);
            textInputLayouts[2].setError("Invalid password");
        }
    }

    @Override
    public void isRegisterButtonVisible(boolean visible) {
        binding.btRegister.setEnabled(visible);
    }

    public void onNavigate() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }


    public void buttonState() {
        textInputEditTexts[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setEmail(s.toString().trim());
                binding.setIsSignUpStateMessageVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textInputEditTexts[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPassword(s.toString().trim());
                binding.setIsSignUpStateMessageVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textInputEditTexts[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setConfirmPassword(s.toString().trim());
                binding.setIsSignUpStateMessageVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
