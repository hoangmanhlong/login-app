package com.example.loginapp.model.interator;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.Constant;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.model.SaveCallback;
import com.example.loginapp.model.listener.MainListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainInteractor {

    private final String TAG = this.toString();

    private final MainListener listener;

    public MainInteractor(MainListener listener) {
        this.listener = listener;
    }

    public void getNumber(Activity activity) {
        String id = Constant.currentUser.getUid();
        Constant.cartRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int backendCount = (int) dataSnapshot.getChildrenCount();
                if (AppSharedPreferences.getInstance(activity).getNumberOfProductInBasket() == -1) {
                    AppSharedPreferences.getInstance(activity).saveNumberOfProductInBasket(backendCount, true);
                }
                if (backendCount > AppSharedPreferences.getInstance(activity).getNumberOfProductInBasket()) {
                    listener.getNumberOfBasketFromServer(backendCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Constant.favoriteProductRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int backendCount = (int) dataSnapshot.getChildrenCount();
                if (AppSharedPreferences.getInstance(activity).getNumberOfProductInWishlist() == -1)
                    AppSharedPreferences.getInstance(activity).saveNumberOfProductInBasket(backendCount, false);
                if (backendCount > AppSharedPreferences.getInstance(activity).getNumberOfProductInWishlist())
                    listener.getNumberOfWishlistFromServer(backendCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
