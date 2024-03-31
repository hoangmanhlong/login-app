package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressInteractor {

    private DeliveryAddressListener listener;

    private String uid;

    private ValueEventListener deliveryAddressValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        deliveryAddresses.add(dataSnapshot.getValue(DeliveryAddress.class));
                    listener.getDeliveryAddress(deliveryAddresses);
                } else {
                    listener.isDeliveryAddressEmpty();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void clear() {
        listener = null;
        deliveryAddressValueEventListener = null;
        uid = null;
    }

    public DeliveryAddressInteractor(DeliveryAddressListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void addDeliveryAddressValueEventListener() {
        if (uid != null)
            Constant.deliveryAddressRef.child(uid)
                .addValueEventListener(deliveryAddressValueEventListener);
    }

    public void removeDeliveryAddressValueEventListener() {
        if (uid != null)
            Constant.deliveryAddressRef.child(uid)
                .removeEventListener(deliveryAddressValueEventListener);
    }
}
