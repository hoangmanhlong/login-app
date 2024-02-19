package com.example.loginapp.view.fragments.verification;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.databinding.FragmentVerificationBinding;
import com.example.loginapp.presenter.VerificationPresenter;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.activities.MainActivity;


public class VerificationFragment extends Fragment implements VerificationView {

    private final VerificationPresenter presenter = new VerificationPresenter(this);

    private FragmentVerificationBinding binding;

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVerificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        binding.setFragment(this);
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        autoFocus();
        dialog = LoadingDialog.getLoadingDialog(requireContext());

        String phoneNumber = getArguments().getString(Constant.PHONE_NUMBER_KEY);
        String formatPhoneNumber = presenter.formatPhoneNumber(phoneNumber);
        binding.setPhoneNumber(formatPhoneNumber);
        presenter.startPhoneNumberVerification(formatPhoneNumber, requireActivity());
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void onVerifyClick() {
        String code = binding.number1.getText().toString() + binding.number2.getText().toString() +
            binding.number3.getText().toString() + binding.number4.getText().toString() +
            binding.number5.getText().toString() + binding.number6.getText().toString();
        presenter.verifyPhoneNumberWithCode(code, requireActivity());
    }

    private void autoFocus() {
        EditText number1 = binding.number1;
        EditText number2 = binding.number2;
        EditText number3 = binding.number3;
        EditText number4 = binding.number4;
        EditText number5 = binding.number5;
        EditText number6 = binding.number6;
        number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) number2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) number3.requestFocus();
                if (s.length() == 0) {
                    number2.clearFocus();
                    number1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) number4.requestFocus();
                if (s.length() == 0) {
                    number3.clearFocus();
                    number2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) number5.requestFocus();
                if (s.length() == 0) {
                    number4.clearFocus();
                    number3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        number5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) number6.requestFocus();
                if (s.length() == 0) {
                    number5.clearFocus();
                    number4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        number6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    number6.clearFocus();
                    number5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void enableVerifyButton() {
        binding.btVerify.setEnabled(true);
    }

    @Override
    public void isLoading(Boolean check) {
        if (check) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void goHomeScreen() {
        startActivity(new Intent(requireActivity(), MainActivity.class));
    }
}