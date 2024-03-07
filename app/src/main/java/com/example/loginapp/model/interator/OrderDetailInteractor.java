package com.example.loginapp.model.interator;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.listener.OrderDetailListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class OrderDetailInteractor {

    private final String TAG = this.toString();

    private final OrderDetailListener listener;

    private final DatabaseReference orderRefOfCurrentUser = Constant.orderRef.child(FirebaseAuth.getInstance().getUid());

    public OrderDetailInteractor(OrderDetailListener listener) {
        this.listener = listener;
    }

    public void cancelOrder(Order order) {
        orderRefOfCurrentUser.child(order.getOrderId())
                .setValue(order)
                .addOnCompleteListener(task -> listener.isProcessSuccess(task.isSuccessful()))
                .addOnFailureListener(e -> listener.isProcessSuccess(false));
    }

    public void buyAgain(Order newOrder) {
        orderRefOfCurrentUser.child(newOrder.getOrderId()).setValue(newOrder)
                .addOnCompleteListener(task -> listener.isProcessSuccess(task.isSuccessful()));
    }

}
