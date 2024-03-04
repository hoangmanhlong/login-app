package com.example.loginapp.view.fragments.voucher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginapp.adapter.voucher_adapter.VoucherAdapter;
import com.example.loginapp.databinding.FragmentAllVouchersBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.AllVouchersPresenter;

import java.util.List;


public class AllVouchersFragment extends Fragment implements  AllVouchersView {

    private final AllVouchersPresenter presenter = new AllVouchersPresenter(this);

    private FragmentAllVouchersBinding binding;

    private final VoucherAdapter voucherAdapter = new VoucherAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllVouchersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.allVoucherRecyclerView.setAdapter(voucherAdapter);
        presenter.getAllVouchers();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        voucherAdapter.submitList(vouchers);
    }

    @Override
    public void isLoading(Boolean loading) {

    }

    @Override
    public void isMyVouchersEmpty(boolean isEmpty) {
        binding.setIsAllVouchersEmpty(isEmpty);
    }
}