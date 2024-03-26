package com.example.loginapp.view.commonUI;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.loginapp.R;

public final class AppConfirmDialog {

    public static void show(
            Context context,
            String title,
            String message,
            AppConfirmDialogButtonListener listener
    ) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_confirm_popup);
        ((TextView) dialog.findViewById(R.id.tvPopupTitle)).setText(title);
        ((TextView) dialog.findViewById(R.id.tvPopupMessage)).setText(message);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_rounded_background));

        ((Button) dialog.findViewById(R.id.btPositive)).setOnClickListener(v -> {
            listener.onPositiveButtonClickListener();
            dialog.dismiss();
        });

        ((Button) dialog.findViewById(R.id.btNegative)).setOnClickListener(v -> {
            listener.onNegativeButtonClickListener();
            dialog.dismiss();
        });
        dialog.show();
    }

    public interface AppConfirmDialogButtonListener {
        void onPositiveButtonClickListener();

        void onNegativeButtonClickListener();
    }
}
