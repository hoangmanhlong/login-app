package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.CartListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartInterator {

    private final String TAG = this.toString();

    private final DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child(Constant.VOUCHER_REF);

    private final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child(Constant.CART_REF);

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final CartListener listener;

    public CartInterator(CartListener listener) {
        this.listener = listener;
    }

    public void getFavoriteProductFromFirebase() {
        assert currentUser != null;
        String id = currentUser.getUid();
        cartRef.child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseProduct product = snapshot.getValue(FirebaseProduct.class);
                assert product != null;
                Log.d(TAG, product.getTitle());
                listener.notifyItemAdded(product, previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseProduct product = snapshot.getValue(FirebaseProduct.class);
                listener.notifyItemChanged(product, previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FirebaseProduct product = snapshot.getValue(FirebaseProduct.class);
                listener.notifyItemRemoved(product);
            }

            @Override
            public void onChildMoved(
                    @NonNull DataSnapshot snapshot,
                    @Nullable String previousChildName
            ) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateQuantity(int id, int quantity) {
        cartRef.child(currentUser.getUid()).child(String.valueOf(id))
                .child("quantity").setValue(String.valueOf(quantity));
    }

    public void updateChecked(int id, boolean checked) {
        cartRef.child(currentUser.getUid()).child(String.valueOf(id)).child("checked").setValue(checked);
    }

    public void deleteProductInFirebase(int id) {
        cartRef.child(currentUser.getUid()).child(String.valueOf(id)).removeValue();
    }

    public void getVoucher() {
        voucherRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listener.getVouchers(snapshot.getValue(Voucher.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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
