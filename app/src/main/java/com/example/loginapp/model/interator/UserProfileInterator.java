package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.UserProfileDetailListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileInterator {

    private UserProfileDetailListener listener;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public UserProfileInterator(UserProfileDetailListener listener) {
        this.listener = listener;
    }

    public void clear() {
        listener = null;
        user = null;
    }

    private final ValueEventListener userDataValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    listener.getUserData(snapshot.getValue(UserData.class));
                } else {
                    listener.userDataEmpty();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private final ValueEventListener ordersValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    List<Order> orders = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        orders.add(dataSnapshot.getValue(Order.class));
                    listener.getOrders(orders);
                } else {
                    listener.isOrdersListEmpty();
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void addUserDataValueEventListener() {
        Constant.userRef.child(user.getUid()).addValueEventListener(userDataValueEventListener);
    }

    public void removeUserDataValueEventListener() {
        Constant.userRef.child(user.getUid()).removeEventListener(userDataValueEventListener);
    }

    public void addOrdersValueEventListener() {
        Constant.orderRef.child(user.getUid()).addValueEventListener(ordersValueEventListener);
    }

    public void removeOrdersValueEventListener() {
        Constant.orderRef.child(user.getUid()).removeEventListener(ordersValueEventListener);
    }

}
