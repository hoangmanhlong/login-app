package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainInteractor {

    private final MainListener listener;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listener.getNumberOfProductInShoppingCart((int) snapshot.getChildrenCount(), !snapshot.exists());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private final DatabaseReference cartRef = Constant.cartRef;

    public MainInteractor(MainListener listener) {
        this.listener = listener;
    }

    public void addValueEventListener() {
        if (user != null)
            cartRef.child(user.getUid()).addValueEventListener(valueEventListener);
    }

    public void removeValueEventListener() {
        cartRef.removeEventListener(valueEventListener);
    }
}
