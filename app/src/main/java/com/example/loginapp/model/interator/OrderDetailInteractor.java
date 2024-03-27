package com.example.loginapp.model.interator;

import android.util.Log;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.listener.OrderDetailListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class OrderDetailInteractor {

    private final String TAG = OrderDetailInteractor.class.getSimpleName();

    private OrderDetailListener listener;

    private DatabaseReference orderRefOfCurrentUser = null;

    public OrderDetailInteractor(OrderDetailListener listener) {
        this.listener = listener;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) orderRefOfCurrentUser = Constant.orderRef.child(uid);
    }

    public void clear() {
        listener = null;
        orderRefOfCurrentUser = null;
    }

    public void cancelOrder(Order order) {
        if (orderRefOfCurrentUser != null)
            orderRefOfCurrentUser.child(order.getOrderId())
                    .setValue(order)
                    .addOnCompleteListener(task -> {
                        if (listener != null) {
                            listener.isProcessSuccess(task.isSuccessful());
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (listener != null) listener.isProcessSuccess(false);
                        Log.e(TAG, "cancelOrder: " + e.getMessage());
                    });
    }

}
