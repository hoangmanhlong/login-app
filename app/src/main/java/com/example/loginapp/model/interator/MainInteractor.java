package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainInteractor {

    private final String TAG = MainInteractor.class.getName();

    private final MainListener listener;

    public MainInteractor(MainListener listener) {
        this.listener = listener;
    }

    public void getNavigationState(String uid) {
        Constant.cartRef.child(uid).addValueEventListener(new ValueEventListener() {
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
