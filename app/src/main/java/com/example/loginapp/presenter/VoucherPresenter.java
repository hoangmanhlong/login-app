package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.VoucherInteractor;
import com.example.loginapp.model.listener.VoucherListener;
import com.example.loginapp.view.fragments.voucher.VoucherView;

import java.util.List;

public class VoucherPresenter implements VoucherListener {

    private final VoucherView view;

    private final VoucherInteractor interactor = new VoucherInteractor(this);

    public VoucherPresenter(VoucherView view) {
        this.view = view;
    }

    public void getVouchers() {
        interactor.getVouchers();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        view.isVouchersEmpty(false);
        view.bindVouchers(vouchers);
    }

    @Override
    public void isVouchersEmpty() {
        view.isVouchersEmpty(true);
    }
}
