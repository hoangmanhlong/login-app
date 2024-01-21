package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.listener.FavoriteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavoriteInterator {
    private final DatabaseReference favoriteRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.FAVORITE_PRODUCT_REF);

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private FavoriteListener listener;

    public FavoriteInterator(FavoriteListener listener) {
        this.listener = listener;
    }

    public void getFavoriteProductFromFirebase() {
        assert currentUser != null;
        String id = currentUser.getUid();
        favoriteRef.child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(
                @NonNull DataSnapshot snapshot,
                @Nullable String previousChildName
            ) {
                Product product = snapshot.getValue(Product.class);
                listener.onItemAdded(product);
            }

            @Override
            public void onChildChanged(
                @NonNull DataSnapshot snapshot,
                @Nullable String previousChildName
            ) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
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
}
