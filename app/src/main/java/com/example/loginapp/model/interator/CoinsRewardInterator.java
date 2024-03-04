package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.AttendanceManager;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.CoinsRewardListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoinsRewardInterator {

    private final String TAG = this.toString();

    private final CoinsRewardListener listener;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private List<Voucher> allVouchers = new ArrayList<>();

    public CoinsRewardInterator(CoinsRewardListener listener) {
        this.listener = listener;
    }

    public void attendance(AttendanceManager attendanceManager) {
        Constant.coinsRef.child(user.getUid()).setValue(attendanceManager)
                .addOnCompleteListener(s -> listener.iAttendanceSuccess(s.isSuccessful()))
                .addOnCompleteListener(e -> listener.iAttendanceSuccess(false));
    }

    public void getAttendanceData() {

        Query query = Constant.coinsRef.child(user.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) listener.getAttendanceData(snapshot.getValue(AttendanceManager.class));
                else listener.isCheckedDayEmpty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.getDataError();
            }
        });
    }

    public void getAllVouchers() {

        Constant.voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Voucher> vouchers = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        vouchers.add(dataSnapshot.getValue(Voucher.class));
                    listener.getVouchers(vouchers);
                } else {
                    // Handle case when there are no all vouchers
                    listener.isVouchersListEmpty(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getMyVouchers() {
        Constant.myVoucherRef.child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Voucher> vouchers = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                vouchers.add(dataSnapshot.getValue(Voucher.class));
                            listener.getMyVouchers(vouchers);
                        } else {
                            listener.isMyVoucherEmpty();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
                                .addOnCompleteListener(myVoucherTask -> listener.isRedeemSuccess(myVoucherTask.isSuccessful()))
                                .addOnFailureListener(e -> listener.isRedeemSuccess(false));
                    }
                });
    }
}
