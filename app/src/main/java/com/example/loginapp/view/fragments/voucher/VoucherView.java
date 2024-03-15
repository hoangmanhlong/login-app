package com.example.loginapp.view.fragments.voucher;

import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface VoucherView {

    void bindVouchers(List<Voucher> vouchers);

    void isVouchersEmpty(boolean isEmpty);
}
