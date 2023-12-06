package com.example.loginapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityLoginBinding;
import com.example.loginapp.presenter.AppPresenter;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AppPresenter appPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPresenter = new AppPresenter(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLoginActivity(this);

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
        appPresenter.putUserInput(email, password);
    }

    public void showSuccess() {
        Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
    }

    public void showFail() {
        Toast.makeText(this, "Không hợp lệ!", Toast.LENGTH_SHORT).show();
    }
}
