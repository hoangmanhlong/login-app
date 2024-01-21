package com.example.loginapp.model.interator;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.model.listener.LoginListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginInteractor {
    private final String TAG = this.toString();

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final LoginListener listener;

    public LoginInteractor(LoginListener listener) {
        this.listener = listener;
        // To apply the default app language instead of explicitly setting it.
        mAuth.useAppLanguage();
    }

    public void loginWithEmail(String email, String password, Activity activity) {
        if (!isValidEmail(email) || !isPasswordValid(password)) {
            listener.isLoginFailed(true);
            listener.onShowProcessBar(false);
        } else {
            listener.onShowProcessBar(true);
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        listener.onShowProcessBar(false);
                        listener.goHomeScreen();
                    } else {
                        listener.isLoginFailed(true);
                        listener.onShowProcessBar(false);
                    }
                });
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

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }


}
