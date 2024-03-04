package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.AttendanceManager;
import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface CoinsRewardListener {

   void getAttendanceData(AttendanceManager manager);

   void iAttendanceSuccess(boolean isSuccess);

   void isCheckedDayEmpty();

   void getDataError();

   void getVouchers(List<Voucher> vouchers);

   void getMyVouchers(List<Voucher> vouchers);

   void isMyVoucherEmpty();

   void isVouchersListEmpty(boolean isEmpty);

   void isRedeemSuccess(boolean isSuccess);
}
