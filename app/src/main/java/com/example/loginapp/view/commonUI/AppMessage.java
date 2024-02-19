package com.example.loginapp.view.commonUI;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

public final class AppMessage {
    public static void showMessage(@NonNull Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
