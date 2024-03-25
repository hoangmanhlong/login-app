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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentRegisterBinding;
import com.example.loginapp.presenter.RegisterPresenter;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment implements RegisterView {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private FragmentRegisterBinding binding;

    private RegisterPresenter presenter;

    private TextInputLayout[] textInputLayouts;

    private TextInputEditText[] textInputEditTexts;

    private Dialog dialog;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new RegisterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.setRegisterFragment(this);
        textInputLayouts = new TextInputLayout[]{binding.emailTextInputLayout, binding.passwordTextInputLayout};
        textInputEditTexts = new TextInputEditText[]{binding.etEmail, binding.etPassword};
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
    public void signupSuccess() {
        int startDestinationId = navController.getGraph().getStartDestinationId();
        navController.popBackStack(startDestinationId, true);
        navController.navigate(startDestinationId);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        textInputLayouts = null;
        textInputEditTexts = null;
        dialog = null;
    }

    @Override
    public void isRegisterButtonVisible(boolean visible) {
        binding.btRegister.setEnabled(visible);
    }

    public void onNavigate() {
        navController.navigateUp();
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
    }
}
