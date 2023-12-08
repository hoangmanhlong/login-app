package com.example.loginapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityLoginBinding;
import com.example.loginapp.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginPresenter loginPresenter;

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            Log.d(this.toString(), String.valueOf(result.getResultCode()));
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");
                    binding.emailInput.setText(email);
                    binding.passwordInput.setText(password);
                    Log.d(this.toString(), email.toString());
                    Log.d(this.toString(), password.toString());
                }
            }
        }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter(this);
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
        loginPresenter.putUserInput(email, password);
    }

    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onCreateAccountBtn() {
        Intent intent = new Intent(this, RegisterActivity.class);
        mGetContent.launch(intent);
    }

    public void goHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
    }

}
