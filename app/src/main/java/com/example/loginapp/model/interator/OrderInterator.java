package com.example.loginapp.model.interator;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.listener.OrderListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderInterator {

    private final OrderListener listener;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    public OrderInterator(OrderListener listener) {
        this.listener = listener;
    }

    public void getOrders() {
        List<Order> orders = new ArrayList<>();
        Constant.orderRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    orders.add(order);

                }
                listener.getOrders(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
