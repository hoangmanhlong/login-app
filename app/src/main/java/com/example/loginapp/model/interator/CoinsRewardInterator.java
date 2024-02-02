package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.Coin;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoinsRewardInterator {

    private final CoinsRewardListener listener;

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
        Constant.coinsRef.child(Constant.currentUser.getUid()).setValue(
                new Coin(list, 100)
        );
    }

    public void getCalendar() {
        Constant.coinsRef.child(Constant.currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listener.getCalendar(snapshot.getValue(Coin.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
