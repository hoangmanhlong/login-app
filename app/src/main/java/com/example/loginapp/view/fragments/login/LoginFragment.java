package com.example.loginapp.view.fragments.login;

import android.app.Dialog;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentLoginBinding;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.utils.NetworkChecker;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.google.android.material.textfield.TextInputEditText;

/**
 * This screen is used to log in to your account
 */
public class LoginFragment extends Fragment implements LoginView {

    // refer [home fragment] layout using view binding
    private FragmentLoginBinding binding;

    private LoginPresenter presenter;

    // Use for navigation
    private NavController navController;

    // use used when authenticating users
    private Dialog dialog;

    // holder ref to email, password field
    private TextInputEditText[] editTexts;

    // holder ref to phone number field
    private EditText etPhoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new LoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setLoginFragment(this);
        etPhoneNumber = binding.etPhoneNumber;
        editTexts = new TextInputEditText[]{binding.emailTextInputEditText, binding.passwordTextInputEditText};
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        // Keyboard pressing when clicking on view is not editText
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        presenter.initData();

        // observer email, password input
        loginWithEmailButtonObserver();

        // observer phone number input
        phoneNumberEditTextListener();
    }

    private void phoneNumberEditTextListener() {
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Save input into presenter
                presenter.setPhoneNumber(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // update UI when phone number view clicked
    public void onNumberPhoneBtn() {
        binding.numberPhoneInput.setVisibility(View.VISIBLE);
        binding.linearInputEmail.setVisibility(View.GONE);
        binding.loginPhoneNumber.setBackgroundResource(R.drawable.background_login_select);
        binding.loginEmail.setBackgroundResource(R.drawable.wrap_select_option_login_background);
    }

    // update UI when email view clicked
    public void onEmailClick() {
        binding.numberPhoneInput.setVisibility(View.GONE);
        binding.linearInputEmail.setVisibility(View.VISIBLE);
        binding.loginPhoneNumber.setBackgroundResource(R.drawable.wrap_select_option_login_background);
        binding.loginEmail.setBackgroundResource(R.drawable.background_login_select);
    }

    // called when login with email button clicked
    public void onLoginWithEmailClick() {
        // Check network before authentication
        if (NetworkChecker.isNetworkAvailable(requireContext()))
            // Authenticate email and password
            presenter.loginWithEmail();
    }

    /**
     * Called when login with email button clicked, navigate to
     * {@link com.example.loginapp.view.fragments.register.RegisterFragment}
     */
    public void goRegisterScreen() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }

    // remove all ref view - avoid memory leak
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        editTexts = null;
        etPhoneNumber = null;
    }

    // remove all data when fragment destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
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

    // Called again when account authentication begins and completes
    @Override
    public void isLoading(@NonNull Boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
    }

    // Called again when authentication is successful
    @Override
    public void isLoginSuccess(boolean isSuccess) {
        // Hide loading Dialog
        dialog.dismiss();
        if (isSuccess) {
            // navigate to MainFragment
            int startDestinationId = navController.getGraph().getStartDestinationId();
            if (navController.getPreviousBackStackEntry() != null) {
                navController.popBackStack(startDestinationId, true);
                navController.navigate(startDestinationId);
            } else {
                navController.popBackStack(R.id.loginFragment, true);
                navController.navigate(startDestinationId);
            }
        } else {
            // is done when authentication ís failure
            binding.setLoginEmailAndPasswordMessageVisible(true);
        }
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    // Called again when phone number is valid or not
    @Override
    public void requestOTPButtonEnabled(boolean enabled) {
        binding.setLoginWithPhoneNumberButtonEnabled(enabled);
    }

    // Called again when email, password is valid or not
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
