package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.SelectVoucherInterator;
import com.example.loginapp.model.listener.SelectVoucherListener;
import com.example.loginapp.view.fragments.select_voucher_fragment.SelectVoucherView;

import java.util.List;

public class SelectVoucherPresenter implements SelectVoucherListener {

    private SelectVoucherInterator interator;

    private SelectVoucherView view;

    private Voucher selectedVoucher;

    public Voucher getSelectedVoucher() {
        return selectedVoucher;
    }

    public void setSelectedVoucher(Voucher selectedVoucher) {
        this.selectedVoucher = selectedVoucher;
    }

    public void clear() {
        view = null;
        selectedVoucher = null;
        interator.clear();
        interator = null;
    }

    public SelectVoucherPresenter(SelectVoucherView view) {
        this.view = view;
        interator = new SelectVoucherInterator(this);
    }

    public void getVoucher() {
        interator.getVoucher();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        view.getVouchers(vouchers);
    }

    @Override
    public void isVouchersEmpty() {
        view.isVouchersEmpty();
    }
}
