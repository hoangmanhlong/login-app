package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.model.interator.LoginInteractor;
import com.example.loginapp.model.listener.LoginListener;
import com.example.loginapp.view.fragments.login.LoginView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPresenter implements LoginListener {

    private final LoginView view;

    private final LoginInteractor interactor = new LoginInteractor(this);

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    @Override
    public void loginStatus(Boolean success) {
        view.isLoading(false);
        view.isLoginSuccess(success);
    }

    public void loginWithEmail(String email, String password, Activity activity) {
        if (isValidEmail(email) && password.length() >= 6) {
            view.isLoading(true);
            interactor.loginWithEmail(email, password, activity);
        } else {
            view.isLoginSuccess(false);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\" +
                        ".[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
