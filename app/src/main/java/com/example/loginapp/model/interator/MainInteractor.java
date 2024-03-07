package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainInteractor {

    private final MainListener listener;

    public MainInteractor(MainListener listener) {
        this.listener = listener;
    }

    public void getNumberOfShoppingCart() {
        Constant.cartRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listener.getNumberOfProductInShoppingCart((int) snapshot.getChildrenCount(), !snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
