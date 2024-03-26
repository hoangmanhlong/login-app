package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.FavoriteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteInterator {

    private FavoriteListener listener;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ValueEventListener favoriteListValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    List<Product> products = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        products.add(dataSnapshot.getValue(Product.class));
                    listener.bindFavoriteListProduct(products);
                } else {
                    listener.isWishlistEmpty();
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public FavoriteInterator(FavoriteListener listener) {
        this.listener = listener;
    }

    public void addFavoriteListValueEventListener() {
        Constant.favoriteProductRef.child(user.getUid())
                .addValueEventListener(favoriteListValueEventListener);
    }

    public void removeFavoriteListValueEventListener() {
        Constant.favoriteProductRef.child(user.getUid())
                .removeEventListener(favoriteListValueEventListener);
    }

    public void deleteProduct(int id) {
        Constant.favoriteProductRef.child(user.getUid())
                .child(String.valueOf(id)).removeValue()
                .addOnFailureListener(e -> listener.onMessage("Error"));
    }

    public void clearData() {
        listener = null;
        favoriteListValueEventListener = null;
        user = null;
    }
}
