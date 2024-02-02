package com.example.loginapp.adapter.select_voucher_adapter;

import com.example.loginapp.model.entity.Voucher;

public interface OnSelectVoucherClickListener {
    void onItemClick(Voucher voucher);

    void enableOkButton();
}
