package com.example.loginapp.view.fragments.voucher;

import com.example.loginapp.model.entity.Voucher;

import java.util.List;

public interface AllVouchersView {

    void getVouchers(List<Voucher> vouchers);

    void isLoading(Boolean loading);

    void isMyVouchersEmpty(boolean isEmpty);
}
