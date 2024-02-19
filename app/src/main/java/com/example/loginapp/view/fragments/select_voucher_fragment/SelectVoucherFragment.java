package com.example.loginapp.view.fragments.select_voucher_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.adapter.select_voucher_adapter.OnSelectVoucherClickListener;
import com.example.loginapp.adapter.select_voucher_adapter.SelectVoucherAdapter;
import com.example.loginapp.databinding.FragmentSelectVoucherBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.SelectVoucherPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class SelectVoucherFragment extends Fragment implements SelectVoucherView, OnSelectVoucherClickListener {

    private final SelectVoucherPresenter presenter = new SelectVoucherPresenter(this);

    private FragmentSelectVoucherBinding binding;

    private final SelectVoucherAdapter adapter = new SelectVoucherAdapter(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectVoucherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        binding.voucherRecyclerview.setAdapter(adapter);
        presenter.getVoucher();
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        adapter.submitList(vouchers);
    }

    @Override
    public void onItemClick(Voucher voucher) {
        presenter.setSelectedVoucher(voucher);
    }

    public void onOKButtonClick() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
        EventBus.getDefault().postSticky(new MessageVoucherSelected(presenter.getSelectedVoucher()));
    }

    @Override
    public void enableOkButton() {
        binding.selectVoucherButton.setVisibility(View.VISIBLE);
    }
}