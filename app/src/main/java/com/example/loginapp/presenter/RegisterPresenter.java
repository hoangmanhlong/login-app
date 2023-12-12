package com.example.loginapp.presenter;

import com.example.loginapp.model.Listener;
import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.fragment.RegisterFragment;

public class RegisterPresenter implements Listener {
    private RegisterFragment registerFragment;
    private UserInterator userInterator;

    public RegisterPresenter(RegisterFragment registerFragment) {
        this.registerFragment = registerFragment;
        userInterator = new UserInterator();
    }

    public void register(String email, String password, String confirmPassword) {
        userInterator.registerAccount(registerFragment.getContext(), email, password, confirmPassword, this);
    }

    public void goLoginScreen() {
        registerFragment.goLoginScreen();
    }

    @Override
    public void onSuccess(String message) {
        registerFragment.showSuccess(message);
    }

    @Override
    public void onFail(String message) {
        registerFragment.showFail(message);
    }
}
