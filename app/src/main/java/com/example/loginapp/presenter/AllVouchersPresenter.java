package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.AllVouchersInterator;
import com.example.loginapp.model.listener.AllVoucherListener;
import com.example.loginapp.view.fragments.voucher.AllVouchersView;

import java.util.List;

public class AllVouchersPresenter implements AllVoucherListener {

    private final String TAG = this.toString();

    private final AllVouchersInterator interator = new AllVouchersInterator(this);

    private final AllVouchersView view;

    public AllVouchersPresenter(AllVouchersView view) {
        this.view = view;
    }

    public void getAllVouchers() {
        interator.getAllVouchers();
    }

    @Override
    public void getVouchers(List<Voucher> voucher) {
        Log.d(TAG, "getVouchers: ");
        view.isMyVouchersEmpty(false);
        view.getVouchers(voucher);
    }

    @Override
    public void isMyVoucherEmpty() {
        view.isMyVouchersEmpty(true);
    }
}
