package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.listener.SelectDeliveryAddressListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectDeliveryAddressInteractor {
    private final SelectDeliveryAddressListener listener;

    public SelectDeliveryAddressInteractor(SelectDeliveryAddressListener listener) {
        this.listener = listener;
    }

    public void getDeliveryAddresses() {
        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        Constant.deliveryAddressRef.child(Constant.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    deliveryAddresses.add(dataSnapshot.getValue(DeliveryAddress.class));
                listener.getDeliveryAddresses(deliveryAddresses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
