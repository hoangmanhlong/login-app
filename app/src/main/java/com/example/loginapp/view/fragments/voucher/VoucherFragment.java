package com.example.loginapp.view.fragments.voucher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.adapter.voucher_adapter.VoucherAdapter;
import com.example.loginapp.databinding.FragmentVoucherBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.VoucherPresenter;

import java.util.List;


public class VoucherFragment extends Fragment implements VoucherView {

    private VoucherPresenter presenter;

    private FragmentVoucherBinding binding;

    private VoucherAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new VoucherPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVoucherBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        adapter = new VoucherAdapter();
        binding.myVoucherRecyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getVouchers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
    }

    public void onNavigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void bindVouchers(List<Voucher> vouchers) {
        if (adapter != null) adapter.submitList(vouchers);
    }

    @Override
    public void isVouchersEmpty(boolean isEmpty) {
        if (binding != null) binding.setIsAllVouchersEmpty(isEmpty);
    }
}