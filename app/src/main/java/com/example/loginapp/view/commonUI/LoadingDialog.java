package com.example.loginapp.view.commonUI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.example.loginapp.R;

public class LoadingDialog {
    public static Dialog getLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_loading);
//        Window window = dialog.getWindow();
//        if (window != null) {
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.dimAmount = 0.0f; // Tắt làm mờ
//            window.setAttributes(params);
//        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
