package com.example.loginapp.presenter;

import android.app.Activity;

import com.example.loginapp.model.interator.RegisterInterator;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.view.fragments.register.RegisterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPresenter implements RegisterListener {

    private final RegisterInterator interator = new RegisterInterator(this);

    private final RegisterView view;

    private String email;

    private String password;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
    }

    public void register(String email, String password, String confirmPassword, Activity activity) {
        if (!isValidEmail(email)) {
            view.onMessage("Email format is wrong, Please re-enter");;
        } else if (!isPasswordValid(password)) {
            view.onMessage("Password must be more than 6 characters");
        } else if (!password.equals(confirmPassword)) {
            view.onMessage("Passwords are not duplicates");
        } else {
            view.isLoading(true);
            interator.register(email, password, activity);
            this.email = email;
            this.password = password;
        }
    }

    @Override
    public void isSignupSuccess(Boolean isSuccess) {
        view.isLoading(false);
        if (isSuccess) view.goLoginScreen(email, password);
        else view.onMessage("Error");
    }

    private boolean isValidEmail(String email) {
        String emailRegex =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}
