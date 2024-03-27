package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.VoucherListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VoucherInteractor {

    private VoucherListener listener;

    public VoucherInteractor(VoucherListener listener) {
        this.listener = listener;
    }

    public void clearRef() {
        listener = null;
    }

    public void getVouchers() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            Constant.myVoucherRef.child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (listener != null) {
                                if (snapshot.exists()) {
                                    List<Voucher> vouchers = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                        vouchers.add(dataSnapshot.getValue(Voucher.class));
                                    listener.getVouchers(vouchers);
                                } else {
                                    listener.isVouchersEmpty();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }
}
