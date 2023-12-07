package com.example.loginapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.loginapp.R;
import com.example.loginapp.databinding.ActivityHomeBinding;
import com.example.loginapp.presenter.HomePresenter;

import org.jetbrains.annotations.Nullable;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setHomeActivity(this);
        homePresenter = new HomePresenter(this);

    }

    public void getLogout() {
        homePresenter.logout();
    }

    public void goLogoutScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show();
    }
}