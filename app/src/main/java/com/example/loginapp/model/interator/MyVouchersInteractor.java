package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.MyVouchersListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyVouchersInteractor {

    private final String TAG = this.toString();
    private final MyVouchersListener listener;

    public MyVouchersInteractor(MyVouchersListener listener) {
        this.listener = listener;
    }

    public void getMyVouchers() {
        Query query = Constant.myVoucherRef.child(FirebaseAuth.getInstance().getUid());
        List<Voucher> vouchers = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        vouchers.add(dataSnapshot.getValue(Voucher.class));
                    listener.getMyVouchers(vouchers);
                } else {
                    Log.d(TAG, "onDataChange: snapshot null");
                    listener.isMyVoucherEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
