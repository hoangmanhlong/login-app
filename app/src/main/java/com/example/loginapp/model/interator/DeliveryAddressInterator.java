package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.listener.DeliveryAddressListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DeliveryAddressInterator {

    private final DeliveryAddressListener listener;

    private final Query query = Constant.deliveryAddressRef.child(Constant.currentUser.getUid());

    public DeliveryAddressInterator(DeliveryAddressListener listener) {
        this.listener = listener;
    }

    public void getShippingAddresses() {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            listener.notifyItemAdded(snapshot.getValue(DeliveryAddress.class));
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
                } else {
                    listener.isDeliveryAddressesEmpty(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
