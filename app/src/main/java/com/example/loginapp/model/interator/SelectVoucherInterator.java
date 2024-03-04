package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.SelectVoucherListener;
import com.google.firebase.auth.FirebaseAuth;
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
        Constant.myVoucherRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                vouchers.add(dataSnapshot.getValue(Voucher.class));
                            listener.getVouchers(vouchers);
                        } else {
                            listener.isVouchersEmpty();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
