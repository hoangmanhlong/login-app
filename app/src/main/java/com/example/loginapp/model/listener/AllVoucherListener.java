package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface AllVoucherListener {
    void getVouchers(List<Voucher> voucher);

    void isMyVoucherEmpty();
}
