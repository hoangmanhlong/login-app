package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.UserProfileListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserProfileInterator {

    private final UserProfileListener listener;

    public UserProfileInterator(UserProfileListener listener) {
        this.listener = listener;
    }

    public void getUserData() {
        Constant.userRef.child(Constant.currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.getUserData(snapshot.getValue(UserData.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getOrderStatus() {
        
    }
}
