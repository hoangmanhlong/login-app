package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.UserProfileDetailListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileInteractor {

    private static final String TAG = UserProfileInteractor.class.getSimpleName();

    private UserProfileDetailListener listener;

    private String uid;

    private ValueEventListener userDataValueEventListener = new ValueEventListener() {
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

    private ValueEventListener ordersValueEventListener = new ValueEventListener() {
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

    public UserProfileInteractor(UserProfileDetailListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void clear() {
        listener = null;
        uid = null;
        userDataValueEventListener = null;
        ordersValueEventListener = null;
    }

    public void addUserDataValueEventListener() {

        if (uid != null)
            Constant.userRef.child(uid).addValueEventListener(userDataValueEventListener);
    }

    public void removeUserDataValueEventListener() {
        if (uid != null)
            Constant.userRef.child(uid).removeEventListener(userDataValueEventListener);
    }

    public void addOrdersValueEventListener() {
        if (uid != null)
            Constant.orderRef.child(uid).addValueEventListener(ordersValueEventListener);
    }

    public void removeOrdersValueEventListener() {
        if (uid != null)
            Constant.orderRef.child(uid).removeEventListener(ordersValueEventListener);
    }
}
