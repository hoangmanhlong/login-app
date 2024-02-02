package com.example.loginapp.view.fragment.select_voucher_fragment;

import com.example.loginapp.model.entity.Voucher;

public class MessageVoucherSelected {

    private Voucher voucher;

    public MessageVoucherSelected(Voucher voucher) {
        this.voucher = voucher;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    @Override
    public String toString() {
        return "MessageVoucherSelected{" +
                "voucher=" + voucher +
                '}';
    }
}
