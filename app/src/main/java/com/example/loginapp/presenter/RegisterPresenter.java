package com.example.loginapp.presenter;

import androidx.annotation.StringRes;

import com.example.loginapp.model.interator.RegisterInterator;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.view.fragments.register.RegisterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPresenter implements RegisterListener {

    private final RegisterInterator interator;

    private final RegisterView view;

    private String email;

    private String password;

    private boolean isEmailValid = false;

    private boolean isPasswordValid = false;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        interator = new RegisterInterator(this);
    }

    public void register() {
        view.isLoading(true);
        interator.register(email, password);
    }

    public void setEmail(String email) {
        isEmailValid = isValidEmail(email);
        this.email = email;
        view.isValidEmail(isEmailValid);
        view.isRegisterButtonVisible(isRegisterButtonVisible());
    }

    public void setPassword(String password) {
        isPasswordValid = password.length() >= 6;
        this.password = password;
        view.isValidPassword(isPasswordValid);
        view.isRegisterButtonVisible(isRegisterButtonVisible());
    }

    private boolean isRegisterButtonVisible() {
        return isEmailValid && isPasswordValid;
    }

    @Override
    public void onSignupSuccess() {
        view.isLoading(false);
        view.signupSuccess();
    }

    @Override
    public void onSignupError(@StringRes int message) {
        view.isLoading(false);
        view.onMessage(message);
    }

    private boolean isValidEmail(String email) {
        String emailRegex =
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
