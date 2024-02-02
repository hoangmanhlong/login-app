package com.example.loginapp.view;

import android.content.Context;
import android.widget.Toast;

public final class AppMessage {
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
