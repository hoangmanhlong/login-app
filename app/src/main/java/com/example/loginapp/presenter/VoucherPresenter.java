package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.VoucherInteractor;
import com.example.loginapp.model.listener.VoucherListener;
import com.example.loginapp.view.fragments.voucher.VoucherView;

import java.util.List;

public class VoucherPresenter implements VoucherListener {

    private VoucherView view;

    private VoucherInteractor interactor;

    public VoucherPresenter(VoucherView view) {
        this.view = view;
        interactor = new VoucherInteractor(this);
    }

    public void getVouchers() {
        interactor.getVouchers();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        if (view != null) {
            view.isVouchersEmpty(false);
            view.bindVouchers(vouchers);
        }
    }

    @Override
    public void isVouchersEmpty() {
        if (view != null) view.isVouchersEmpty(true);
    }

    public void clear() {
        view = null;
        interactor.clear();
        interactor = null;
    }
}
