package com.example.loginapp.view.fragment.register;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentRegisterBinding;
import com.example.loginapp.model.MyOpenDocumentContract;
import com.example.loginapp.presenter.RegisterPresenter;
import com.example.loginapp.view.state.RegisterButtonObserver;

public class RegisterFragment extends Fragment implements RegisterView {
    private FragmentRegisterBinding binding;
    private RegisterPresenter registerPresenter;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setRegisterFragment(this);
        registerPresenter = new RegisterPresenter(this);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(requireContext(), view);
                return false;
            }
        });

        // Observer register button
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

    public void register() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordInput.getText().toString().trim();
        registerPresenter.register(email, password, confirmPassword, requireActivity());
    }

    public void goLoginScreen() {
        Bundle bundle = new Bundle();
        bundle.putString("email", binding.emailInput.getText().toString());
        bundle.putString("password", binding.passwordInput.getText().toString());
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_registerFragment_to_loginFragment, bundle);
    }

    @Override
    public void onRegisterMessage(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowProcessBar(Boolean show) {
        if (show) {
            binding.registerProcessBar.setVisibility(View.VISIBLE);
            binding.registerButton.setVisibility(View.GONE);
        } else {
            binding.registerProcessBar.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.VISIBLE);
        }
    }

    public void onNavigate() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
