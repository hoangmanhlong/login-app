package com.example.loginapp.presenter;

import com.example.loginapp.model.Listener;
import com.example.loginapp.model.UserInterator;
import com.example.loginapp.view.fragment.LoginFragment;

public class LoginPresenter implements Listener {
    private LoginFragment loginFragment;
    private UserInterator userInterator;

    public LoginPresenter(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
        userInterator = new UserInterator();
    }

    public void goHomeScreen() {
        loginFragment.goHomeScreen();
    }

    public void putUserInput(String email, String password) {
        userInterator.login(loginFragment.getContext(), email, password, this);
    }

    @Override
    public void onSuccess(String message) {
        loginFragment.showSuccess(message);
    }

    @Override
    public void onFail(String message) {
        loginFragment.showFail(message);
    }
}
