package com.example.loginapp.model.interator;


import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.listener.OrdersListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersInterator {

    private String uid;

    private ValueEventListener ordersValueEventListener;

    public OrdersInterator(OrdersListener listener) {
        uid = FirebaseAuth.getInstance().getUid();
        ordersValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listener != null) {
                    if (snapshot.exists()) {
                        List<Order> orders = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            orders.add(dataSnapshot.getValue(Order.class));
                        listener.getOrders(orders);
                    } else {
                        listener.isOrdersEmpty();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void addOrdersValueEventListener() {
        if (uid != null) {
            Constant.orderRef.child(uid).addValueEventListener(ordersValueEventListener);
        }

    }

    public void removeOrdersValueEventListener() {
        if (uid != null) {
            Constant.orderRef.child(uid).removeEventListener(ordersValueEventListener);
        }

    }

    public void clear() {
        ordersValueEventListener = null;
        uid = null;
    }
}
