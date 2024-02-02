package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.FavoriteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class FavoriteInterator {

    private final FavoriteListener listener;

    public FavoriteInterator(FavoriteListener listener) {
        this.listener = listener;
    }

    public void getFavoriteProductFromFirebase() {
        Constant.favoriteProductRef.child(Constant.currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listener.onItemAdded(snapshot.getValue(Product.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                listener.notifyItemRemoved(snapshot.getValue(Product.class));
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

    public void deleteProduct(int id) {
        Constant.favoriteProductRef.child(Constant.currentUser.getUid())
                .child(String.valueOf(id))
                .removeValue()
                .addOnFailureListener(e -> listener.onMessage("Error"));
    }
}
