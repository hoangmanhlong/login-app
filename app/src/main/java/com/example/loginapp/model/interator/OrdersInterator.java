package com.example.loginapp.model.interator;


import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.listener.OrdersListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersInterator {

    private final OrdersListener listener;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    public OrdersInterator(OrdersListener listener) {
        this.listener = listener;
    }

    public void getOrders() {
        Constant.orderRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Order> orders = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        orders.add(dataSnapshot.getValue(Order.class));
                    listener.getOrders(orders);
                } else {
                    listener.isOrdersEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
