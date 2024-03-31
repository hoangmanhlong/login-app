package com.example.loginapp.view.commonUI;

import androidx.annotation.StringRes;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.OrderStatus;

public final class OrderStatusConverter {
    public static @StringRes int convertToStringRes(OrderStatus orderStatus) {
        @StringRes int statusRes = -1;
        switch (orderStatus) {
            case Processing:
                statusRes = R.string.processing;
                break;
            case Completed:
                statusRes = R.string.completed;
                break;
            case Return:
                statusRes = R.string.return_text;
                break;
            case Cancel:
                statusRes = R.string.cancel;
                break;
        }
        return statusRes;
    }
}
