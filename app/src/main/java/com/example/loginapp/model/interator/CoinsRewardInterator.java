package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Coin;
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
//        Constant.coinsRef.child(Constant.currentUser.getUid())
//                .child("numberOfCoins").setValue(Constant.COINS_FOR_EACH_TIME);
//
//        Constant.coinsRef.child(Constant.currentUser.getUid())
//                .child("rollCalllist").push().setValue(System.currentTimeMillis());
        List<Long> list = new ArrayList<>();
        list.add(System.currentTimeMillis());
        Constant.coinsRef.child(user.getUid()).setValue(
                new Coin(list, 100)
        );
    }

    public void getCalendar() {
        Constant.coinsRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    listener.getCalendar(snapshot.getValue(Coin.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
