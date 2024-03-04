package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.FavoriteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FavoriteInterator {

    private final FavoriteListener listener;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public FavoriteInterator(FavoriteListener listener) {
        this.listener = listener;
    }

    public void getFavoriteProductFromFirebase() {
        Query query = Constant.favoriteProductRef.child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) listener.isWishlistEmpty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query.addChildEventListener(new ChildEventListener() {
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
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void deleteProduct(int id) {
        Constant.favoriteProductRef.child(user.getUid())
                .child(String.valueOf(id)).removeValue()
                .addOnFailureListener(e -> listener.onMessage("Error"));
    }
}
