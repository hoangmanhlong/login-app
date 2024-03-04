package com.example.loginapp.view.fragments.coins;

import com.example.loginapp.model.entity.Date;
import com.example.loginapp.model.entity.DayWithCheck;
import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface CoinsRewardView {

    void isAttendanceLoading(boolean loading);

    void bindNumberOfCoins(int numberOfCoins);

    void getLastDayOfMonth(Date date);

    void bindCheckedDates(List<DayWithCheck> dayWithCheckList);

    void isGetCoinButtonVisible(boolean visible);

    void bindVouchersList(List<Voucher> vouchers);

    void isVouchersListEmpty(boolean isEmpty);

    void onMessage(String message);
}
