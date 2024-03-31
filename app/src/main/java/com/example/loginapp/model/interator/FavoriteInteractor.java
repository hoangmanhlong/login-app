package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.FavoriteListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteInteractor {

    private static final String TAG = FavoriteInteractor.class.getSimpleName();

    private FavoriteListener listener;

    private String uid;

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

    public FavoriteInteractor(FavoriteListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void addFavoriteListValueEventListener() {
        if (uid != null) {
            Constant.favoriteProductRef.child(uid)
                    .addValueEventListener(favoriteListValueEventListener);
        }
    }

    public void removeFavoriteListValueEventListener() {
        if (uid != null) {
            Constant.favoriteProductRef.child(uid)
                    .removeEventListener(favoriteListValueEventListener);
        }
    }

    public void deleteProduct(int id) {
        if (uid != null) {
            Constant.favoriteProductRef.child(uid)
                    .child(String.valueOf(id)).removeValue()
                    .addOnFailureListener(e -> Log.e(TAG, "deleteProduct: " + e.getMessage()));
        }
    }

    public void clearData() {
        listener = null;
        favoriteListValueEventListener = null;
        uid = null;
    }
}
