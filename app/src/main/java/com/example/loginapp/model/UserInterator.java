package com.example.loginapp.model;

import android.content.Context;
import android.util.Log;

import com.example.loginapp.data.AppSharedPreferences;
import com.example.loginapp.presenter.HomePresenter;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.presenter.MainPresenter;
import com.example.loginapp.presenter.RegisterPresenter;

public class UserInterator {
    private final String TAG = this.toString();

    public void login(
        Context context,
        String email,
        String password,
        LoginPresenter loginPresenter
    ) {
        if (email.equals("") || password.equals("")) {
            loginPresenter.onFail("Please enter complete information");
        } else {
            Account account = AppSharedPreferences.getInstance(context).getUserInfo();
            if (account == null) {
                loginPresenter.onFail("Account does not exist");
            } else {
                boolean check =
                    email.equals(account.getEmail()) && password.equals(account.getPassword());
                if (check) {
                    loginPresenter.goHomeScreen();
                    AppSharedPreferences.getInstance(context).setLoginStatus(true);
                    loginPresenter.onSuccess("Logged in successfully");
                } else {
                    loginPresenter.onFail("Account information or password is incorrect");
                }
            }
        }
    }

//    public void fillUserInfo(Context context, LoginPresenter presenter) {
//        Account account = AppSharedPreferences.getInstance(context).getUserInfo();
//        if (account == null) {
//            presenter.fillInfo("", "");
//        } else {
//            presenter.fillInfo(account.getEmail(), account.getPassword());
//        }
//    }

    public void registerAccount(
        Context context,
        String email,
        String password,
        String confirmPassword,
        RegisterPresenter presenter
    ) {
        if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
            presenter.onFail("Please enter complete information");
        } else if (!password.equals(confirmPassword)) {
            presenter.onFail("Passwords are not duplicates");
        } else {
            try {
                AppSharedPreferences.getInstance(context).saveUserAccount(email, password);
                presenter.goLoginScreen();
                presenter.onSuccess("Sign Up Success");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public void isLogged(Context context, MainPresenter presenter) {
        if (AppSharedPreferences.getInstance(context).getLoginStatus()) {
            presenter.goHomeScreen();
        } else {
            Log.d(TAG, "...");
        }
    }

    public void logout(Context context, HomePresenter presenter) {
        AppSharedPreferences.getInstance(context).setLoginStatus(false);
        presenter.goLoginScreen();
    }
}
