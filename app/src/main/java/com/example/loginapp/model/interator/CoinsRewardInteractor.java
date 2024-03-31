package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.AttendanceManager;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoinsRewardInteractor {

    private static final String TAG = CoinsRewardInteractor.class.getSimpleName();

    private CoinsRewardListener listener;

    @Nullable
    private String uid;

    private ValueEventListener attendanceDataValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (listener != null) {
                if (snapshot.exists())
                    listener.getAttendanceData(snapshot.getValue(AttendanceManager.class));
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

    public CoinsRewardInteractor(CoinsRewardListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void clear() {
        myVouchersValueEventListener = null;
        listener = null;
        vouchersValueEventListener = null;
        attendanceDataValueEventListener = null;
        uid = null;
    }

    public void attendance(AttendanceManager attendanceManager) {
        if (uid != null) {
            Constant.coinsRef.child(uid).setValue(attendanceManager)
                    .addOnCompleteListener(s -> listener.iAttendanceSuccess(s.isSuccessful()))
                    .addOnFailureListener(e -> {
                        listener.iAttendanceSuccess(false);
                        Log.e(TAG, "attendance: " + e.getMessage());
                    });
        }

    }

    public void addAttendanceDataValueEventListener() {
        if (uid != null)
            Constant.coinsRef.child(uid)
                    .addValueEventListener(attendanceDataValueEventListener);
    }

    public void removeAttendanceDataValueEventListener() {
        if (uid != null)
            Constant.coinsRef.child(uid)
                    .removeEventListener(attendanceDataValueEventListener);
    }

    public void addVouchersValueEventListener() {
        Constant.voucherRef.addValueEventListener(vouchersValueEventListener);
    }

    public void removeVouchersValueEventListener() {
        Constant.voucherRef.removeEventListener(vouchersValueEventListener);
    }

    public void addMyVouchersValueEventListener() {
        if (uid != null)
            Constant.myVoucherRef.child(uid)
                    .addValueEventListener(myVouchersValueEventListener);
    }

    public void removeMyVouchersValueEventListener() {
        if (uid != null)
            Constant.myVoucherRef.child(uid)
                    .removeEventListener(myVouchersValueEventListener);
    }

    public void redeemVoucher(Voucher voucher, int newNumberOfCoins) {
        if (uid != null) {
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
                                    .addOnFailureListener(e -> Log.e(TAG, "redeemVoucher: " + e.getMessage()));
                        }
                    });
        }

    }
}
