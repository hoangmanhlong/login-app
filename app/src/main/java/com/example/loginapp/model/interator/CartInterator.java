package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.listener.CartListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CartInterator {

    private final String TAG = this.toString();

    private final DatabaseReference cartRef = Constant.cartRef;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private final CartListener listener;

    public CartInterator(CartListener listener) {
        this.listener = listener;
    }

    public void getCartProductsFromFirebase() {
        Query query = cartRef.child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            listener.notifyItemAdded(snapshot.getValue(FirebaseProduct.class));
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            listener.notifyItemChanged(snapshot.getValue(FirebaseProduct.class));
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            listener.notifyItemRemoved(snapshot.getValue(FirebaseProduct.class));
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    listener.isCartEmpty(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateQuantity(int id, int quantity) {
        cartRef.child(user.getUid()).child(String.valueOf(id)).child("quantity").setValue(String.valueOf(quantity));
    }

    public void updateChecked(int id, boolean checked) {
        cartRef.child(user.getUid()).child(String.valueOf(id)).child("checked").setValue(checked);
    }

    public void deleteProductInFirebase(FirebaseProduct product) {
        cartRef.child(user.getUid()).child(String.valueOf(product.getId())).removeValue();
    }
}
