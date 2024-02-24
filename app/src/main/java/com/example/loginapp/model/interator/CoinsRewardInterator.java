package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoinsRewardInterator {

    private final CoinsRewardListener listener;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CoinsRewardInterator(CoinsRewardListener listener) {
        this.listener = listener;
    }

    public void attendance() {

    }

    public void getCalendar() {
//        Constant.coinsRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists())
//                    listener.getCalendar(snapshot.getValue(Coin.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}
