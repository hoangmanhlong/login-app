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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileInterator {

    private final UserProfileDetailListener listener;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public UserProfileInterator(UserProfileDetailListener listener) {
        this.listener = listener;
    }

    public void getUserData() {
        Constant.userRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) listener.getUserData(snapshot.getValue(UserData.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getOrderStatus() {

        Query query = Constant.orderRef.child(user.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Order> orders = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        orders.add(dataSnapshot.getValue(Order.class));
                    listener.getOrders(orders);
                } else {
                    listener.isOrdersListEmpty();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
