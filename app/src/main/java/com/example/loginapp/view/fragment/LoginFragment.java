package com.example.loginapp.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentLoginBinding;
import com.example.loginapp.presenter.LoginPresenter;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginPresenter loginPresenter;

//    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
//        new ActivityResultContracts.StartActivityForResult(),
//        result -> {
//            Log.d(this.toString(), String.valueOf(result.getResultCode()));
//            if (result.getResultCode() == RESULT_OK) {
//                Intent intent = result.getData();
//                if (intent != null) {
//                    String email = intent.getStringExtra("email");
//                    String password = intent.getStringExtra("password");
//                    binding.emailInput.setText(email);
//                    binding.passwordInput.setText(password);
//                    Log.d(this.toString(), email.toString());
//                    Log.d(this.toString(), password.toString());
//                }
//            }
//        }
//    );

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLoginFragment(this);
        hideKeyboardFrom(this.requireContext(), view);
        loginPresenter = new LoginPresenter(this);

        assert getArguments() != null;
        binding.emailInput.setText(getArguments().getString("email"));
        binding.passwordInput.setText(getArguments().getString("password"));

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(requireContext(), view);
                return false;
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

    public void onLogin() {
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();
        loginPresenter.putUserInput(email, password);
    }

    public void showSuccess(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showFail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onCreateAccountBtn() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_loginFragment_to_registerFragment);
    }

    public void goHomeScreen() {
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_loginFragment_to_homeFragment);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
