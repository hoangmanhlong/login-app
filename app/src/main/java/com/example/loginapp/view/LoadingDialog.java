package com.example.loginapp.view;

import android.app.Dialog;
import android.content.Context;

import com.example.loginapp.R;

public class LoadingDialog {
    public static Dialog getLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_loading);
        dialog.setCancelable(false);
        return dialog;
    }
}
