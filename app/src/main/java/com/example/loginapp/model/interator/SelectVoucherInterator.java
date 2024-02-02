package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.SelectVoucherListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectVoucherInterator {

    private final SelectVoucherListener listener;

    public SelectVoucherInterator(SelectVoucherListener listener) {
        this.listener = listener;
    }

    public void getVoucher() {
        List<Voucher> vouchers = new ArrayList<>();
        Constant.voucherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    vouchers.add(dataSnapshot.getValue(Voucher.class));
                listener.getVouchers(vouchers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
