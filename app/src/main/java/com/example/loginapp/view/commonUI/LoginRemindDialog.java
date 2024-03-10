package com.example.loginapp.view.commonUI;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LoginRemindDialog {

    public interface LoginDialogListener {

        void onLoginClicked();

        void onSignUpClicked();
    }

    private static void setupDialog(Context context, LoginDialogListener listener) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.log_in)
                .setMessage(R.string.app_name)
                .setPositiveButton(R.string.sign_in, (dialog, which) -> listener.onLoginClicked())
                .setNegativeButton(R.string.sign_up, (dialog, which) -> listener.onSignUpClicked())
                .setNeutralButton(R.string.later, null)
                .setCancelable(true)
                .show();
    }

    public static void show(Fragment fragment, Context context) {
        setupDialog(context, new LoginRemindDialog.LoginDialogListener() {
            @Override
            public void onLoginClicked() {
                NavHostFragment.findNavController(fragment).navigate(R.id.loginFragment);
            }

            @Override
            public void onSignUpClicked() {
                NavHostFragment.findNavController(fragment).navigate(R.id.registerFragment);
            }
        });
    }
}
