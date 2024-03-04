package com.example.loginapp.view.fragments.voucher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.adapter.voucher_adapter.VoucherAdapter;
import com.example.loginapp.databinding.FragmentMyVouchersBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.MyVouchersPresenter;

import java.util.List;

public class MyVouchersFragment extends Fragment implements MyVouchersView {

    private final MyVouchersPresenter presenter = new MyVouchersPresenter(this);

    private FragmentMyVouchersBinding binding;

    private RecyclerView allVouchersRecyclerView;

    private final VoucherAdapter voucherAdapter = new VoucherAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyVouchersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allVouchersRecyclerView = binding.allVoucherRecyclerView;
        allVouchersRecyclerView.setAdapter(voucherAdapter);
        binding.animationView.setVisibility(View.VISIBLE);
        presenter.getVouchers();
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
        binding.setIsMyVouchersEmpty(isEmpty);
    }
}