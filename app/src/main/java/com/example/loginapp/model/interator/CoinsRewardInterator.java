package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.AttendanceManager;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoinsRewardInterator {

    private final String TAG = this.toString();

    private CoinsRewardListener listener;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ValueEventListener attendanceDataValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) listener.getAttendanceData(snapshot.getValue(AttendanceManager.class));
                else listener.isAttendanceDataEmpty();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ValueEventListener vouchersValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    List<Voucher> vouchers = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        vouchers.add(dataSnapshot.getValue(Voucher.class));
                    listener.getVouchers(vouchers);
                } else {
                    listener.isVouchersListEmpty();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ValueEventListener myVouchersValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists()) {
                    List<Voucher> vouchers = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        vouchers.add(dataSnapshot.getValue(Voucher.class));
                    listener.getMyVouchers(vouchers);
                } else {
                    listener.isMyVoucherEmpty();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public CoinsRewardInterator(CoinsRewardListener listener) {
        this.listener = listener;
    }

    public void clear() {
        myVouchersValueEventListener = null;
        listener = null;
        vouchersValueEventListener = null;
        attendanceDataValueEventListener = null;
        user = null;
    }

    public void attendance(AttendanceManager attendanceManager) {
        Constant.coinsRef.child(user.getUid()).setValue(attendanceManager)
                .addOnCompleteListener(s -> listener.iAttendanceSuccess(s.isSuccessful()))
                .addOnCompleteListener(e -> listener.iAttendanceSuccess(false));
    }

    public void addAttendanceDataValueEventListener() {
        Constant.coinsRef.child(user.getUid()).addValueEventListener(attendanceDataValueEventListener);
    }

    public void removeAttendanceDataValueEventListener() {
        Constant.coinsRef.child(user.getUid()).removeEventListener(attendanceDataValueEventListener);
    }

    public void addVouchersValueEventListener() {
        Constant.voucherRef.addValueEventListener(vouchersValueEventListener);
    }

    public void removeVouchersValueEventListener() {
        Constant.voucherRef.removeEventListener(vouchersValueEventListener);
    }

    public void addMyVouchersValueEventListener() {
        Constant.myVoucherRef.child(user.getUid()).addValueEventListener(myVouchersValueEventListener);
    }

    public void removeMyVouchersValueEventListener() {
        Constant.myVoucherRef.child(user.getUid()).removeEventListener(myVouchersValueEventListener);
    }

    public void redeemVoucher(Voucher voucher, int newNumberOfCoins) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Constant.coinsRef.child(uid).child(AttendanceManager.NUMBER_OF_COINS)
                .setValue(newNumberOfCoins)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Constant.myVoucherRef.child(uid)
                                .child(voucher.getVoucherCode())
                                .setValue(voucher)
                                .addOnCompleteListener(myVoucherTask -> {
                                    if (listener != null)
                                        listener.isRedeemSuccess(myVoucherTask.isSuccessful());
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "redeemVoucher: " + e.getMessage() ));
                    }
                });
    }
}
