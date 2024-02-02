package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.SelectVoucherInterator;
import com.example.loginapp.model.listener.SelectVoucherListener;
import com.example.loginapp.view.fragment.select_voucher_fragment.SelectVoucherView;

import java.util.List;

public class SelectVoucherPresenter implements SelectVoucherListener {

    private final SelectVoucherView view;

    private Voucher selectedVoucher = null;

    public Voucher getSelectedVoucher() {
        return selectedVoucher;
    }

    public void setSelectedVoucher(Voucher selectedVoucher) {
        this.selectedVoucher = selectedVoucher;
    }

    private final SelectVoucherInterator interator = new SelectVoucherInterator(this);

    public SelectVoucherPresenter(SelectVoucherView view) {
        this.view = view;
    }

    public void getVoucher() {
        interator.getVoucher();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        view.getVouchers(vouchers);
    }
}
