package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface MyVouchersListener {

    void isMyVoucherEmpty();

    void getMyVouchers(List<Voucher> vouchers);
}
