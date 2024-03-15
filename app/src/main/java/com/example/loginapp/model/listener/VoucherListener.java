package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface VoucherListener {

    void getVouchers(List<Voucher> vouchers);

    void isVouchersEmpty();
}
