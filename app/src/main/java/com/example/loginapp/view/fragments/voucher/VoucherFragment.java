package com.example.loginapp.view.fragments.voucher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.adapter.voucher_adapter.VoucherAdapter;
import com.example.loginapp.databinding.FragmentVoucherBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.VoucherPresenter;

import java.util.List;


public class VoucherFragment extends Fragment implements VoucherView {

    private final VoucherPresenter presenter = new VoucherPresenter(this);

    private FragmentVoucherBinding binding;

    private final VoucherAdapter adapter = new VoucherAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVoucherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        binding.myVoucherRecyclerView.setAdapter(adapter);
        presenter.getVouchers();
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void bindVouchers(List<Voucher> vouchers) {
        adapter.submitList(vouchers);
    }

    @Override
    public void isVouchersEmpty(boolean isEmpty) {
        binding.setIsAllVouchersEmpty(isEmpty);
    }
}