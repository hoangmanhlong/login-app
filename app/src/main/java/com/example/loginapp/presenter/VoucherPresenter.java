package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.VoucherInterator;
import com.example.loginapp.model.listener.VoucherListener;
import com.example.loginapp.view.fragment.voucher.VoucherView;

import java.util.ArrayList;
import java.util.List;

public class VoucherPresenter implements VoucherListener {
    private final VoucherInterator interator = new VoucherInterator(this);

    public List<Voucher> vouchers = new ArrayList<>();

    private final VoucherView view;

    public VoucherPresenter(VoucherView view) {
        this.view = view;
        getVouchers();
    }

    public void getVouchers() {
        interator.getVoucher();
    }

    @Override
    public void getVouchers(Voucher voucher) {
        vouchers.add(voucher);
        Log.d(this.toString(), "notifyVoucherAdded: " + vouchers.size());
        view.notifyVoucherAdded(vouchers);
    }
}
