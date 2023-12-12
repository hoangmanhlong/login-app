package com.example.loginapp.model;

import android.content.Context;
import android.util.Log;

import com.example.loginapp.data.AppSharedPreferences;
import com.example.loginapp.presenter.HomePresenter;
import com.example.loginapp.presenter.LoginPresenter;
import com.example.loginapp.presenter.OverviewPresenter;
import com.example.loginapp.presenter.RegisterPresenter;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } else if (!isValidEmail(email)) {
            loginPresenter.onFail("Email format is wrong, Please re-enter");
        } else {
            List<Account> accounts = AppSharedPreferences.getInstance(context).getAccounts();
            if (accounts == null) {
                loginPresenter.onFail("Account does not exist");
            } else {
                for (Account account: accounts) {
                    if(account.getEmail().equals(email) && account.getPassword().equals(password)) {
                        AppSharedPreferences.getInstance(context).setLoginStatus(true);
                        loginPresenter.goHomeScreen();
                        loginPresenter.onSuccess("Logged in successfully");
                    } else {
                        loginPresenter.onFail("Account information or password is incorrect");
                    }
                }
            }
        }
    }

    public void registerAccount(
        Context context,
        String email,
        String password,
        String confirmPassword,
        RegisterPresenter presenter
    ) {
        if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
            presenter.onFail("Please enter complete information");
        } else if (!isValidEmail(email)) {
            presenter.onFail("Email format is wrong, Please re-enter");
        } else if (!isPasswordValid(password)) {
            presenter.onFail("Password must be more than 6 characters");
        } else if (!password.equals(confirmPassword)) {
            presenter.onFail("Passwords are not duplicates");
        } else if (!emailIsExists(context, email)) {
            presenter.onFail("Email already exists");
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

    public void isLogged(Context context, OverviewPresenter presenter) {
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

    private boolean isValidEmail(String email) {
        // Biểu thức chính quy để kiểm tra địa chỉ email
        String emailRegex =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Tạo một đối tượng Pattern từ biểu thức chính quy
        Pattern pattern = Pattern.compile(emailRegex);

        // Tạo một đối tượng Matcher từ đối tượng Pattern và đầu vào email
        Matcher matcher = pattern.matcher(email);

        // Kiểm tra xem địa chỉ email có khớp với biểu thức chính quy không
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        // Kiểm tra xem mật khẩu có ít nhất 6 ký tự không
        return password.length() >= 6;
    }

    private boolean emailIsExists(Context context, String email) {
        List<Account> accounts = AppSharedPreferences.getInstance(context).getAccounts();
        if (accounts == null)
            return true;
        else
            for (Account account : accounts)
                if (Objects.equals(account.getEmail(), email))
                    return false;
        return true;
    }
}
