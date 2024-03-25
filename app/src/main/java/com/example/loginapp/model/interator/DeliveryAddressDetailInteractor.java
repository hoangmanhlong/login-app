package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.listener.DeliveryAddressDetailListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressDetailInteractor {

    private static final String TAG = DeliveryAddressDetailInteractor.class.getSimpleName();

    private DeliveryAddressDetailListener listener;

    @Nullable
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    private DatabaseReference ref = (user != null) ? Constant.deliveryAddressRef.child(user.getUid()) : null;

    public DeliveryAddressDetailInteractor(DeliveryAddressDetailListener listener) {
        this.listener = listener;
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        deliveryAddresses.add(dataSnapshot.getValue(DeliveryAddress.class));
                    for (DeliveryAddress deliveryAddress1 : deliveryAddresses)
                        ref.child(deliveryAddress1.getDeliveryAddressId())
                                .child(DeliveryAddress.IS_DEFAULT).setValue(false);
                    update(deliveryAddress);
                } else {
                    update(deliveryAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void update(DeliveryAddress deliveryAddress) {
        ref.child(deliveryAddress.getDeliveryAddressId())
                .setValue(deliveryAddress)
                .addOnCompleteListener(s -> listener.isUpdateSuccess(true))
                .addOnFailureListener(e -> listener.isUpdateSuccess(false));
    }

    public void deleteDeliveryAddress(String id) {
        ref.child(id).removeValue()
                .addOnCompleteListener(task -> listener.deleteSuccess())
                .addOnFailureListener(e -> Log.d(TAG, "deleteDeliveryAddress: " + e.getMessage()));
    }

    public void clearData() {
        listener = null;
        user = null;
        ref = null;
    }
}
