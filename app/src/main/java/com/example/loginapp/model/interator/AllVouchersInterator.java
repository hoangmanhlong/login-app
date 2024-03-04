package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.AllVoucherListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllVouchersInterator {

    private final AllVoucherListener listener;

    public AllVouchersInterator(AllVoucherListener listener) {
        this.listener = listener;
    }

    public void getAllVouchers() {
        List<Voucher> vouchers = new ArrayList<>();

        Constant.voucherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vouchers.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        vouchers.add(dataSnapshot.getValue(Voucher.class));
                    listener.getVouchers(vouchers);
                } else {
                    listener.isMyVoucherEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
