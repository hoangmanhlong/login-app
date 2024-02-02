package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressInterator {

    private final DeliveryAddressListener listener;

    public DeliveryAddressInterator(DeliveryAddressListener listener) {
        this.listener = listener;
    }

    public void getShippingAddresses() {
        Constant.deliveryAddressRef.child(Constant.currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listener.getShippingAddress(snapshot.getValue(DeliveryAddress.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listener.notifyItemChanged(snapshot.getValue(DeliveryAddress.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                listener.notifyItemRemoved(snapshot.getValue(DeliveryAddress.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
