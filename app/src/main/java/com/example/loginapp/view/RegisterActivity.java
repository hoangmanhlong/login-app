package com.example.loginapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.databinding.ActivityRegisterBinding;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.presenter.RegisterPresenter;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setRegisterActivity(this);
        registerPresenter = new RegisterPresenter(this);
    }

    public void register() {
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();
        String confirmPassword = binding.confirmPasswordInput.getText().toString();
        registerPresenter.register(email, password, confirmPassword);
    }

    public void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("email", binding.emailInput.getText().toString());
        intent.putExtra("password", binding.passwordInput.getText().toString());
        setResult(RESULT_OK, intent);
//        startActivity(intent);
        finish();
    }

    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
