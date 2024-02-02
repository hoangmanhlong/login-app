package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.VoucherListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VoucherInterator {

    private final VoucherListener listener;

    public VoucherInterator(VoucherListener listener) {
        this.listener = listener;
    }

    public void getVoucher() {
        List<Voucher> vouchers = new ArrayList<>();
        Constant.voucherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vouchers.clear();
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
