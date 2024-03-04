package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.MyVouchersInteractor;
import com.example.loginapp.model.listener.MyVouchersListener;
import com.example.loginapp.view.fragments.voucher.MyVouchersView;

import java.util.List;

public class MyVouchersPresenter implements MyVouchersListener {

    private final String TAG = this.toString();

    private final MyVouchersInteractor interactor = new MyVouchersInteractor(this);

    private final MyVouchersView view;

    public MyVouchersPresenter(MyVouchersView view) {
        this.view = view;
    }

    public void getVouchers() {
        interactor.getMyVouchers();
    }

    @Override
    public void isMyVoucherEmpty() {
        view.isMyVouchersEmpty(true);
    }

    @Override
    public void getMyVouchers(List<Voucher> vouchers) {
        Log.d(TAG, "getMyVouchers: ");
        view.isMyVouchersEmpty(false);
        view.getVouchers(vouchers);
    }
}
