package com.example.loginapp.view.fragments.select_voucher_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.adapter.select_voucher_adapter.OnSelectVoucherClickListener;
import com.example.loginapp.adapter.select_voucher_adapter.SelectVoucherAdapter;
import com.example.loginapp.databinding.FragmentSelectVoucherBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.presenter.SelectVoucherPresenter;
import com.example.loginapp.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class SelectVoucherFragment extends Fragment implements SelectVoucherView, OnSelectVoucherClickListener {

    private SelectVoucherPresenter presenter;

    private FragmentSelectVoucherBinding binding;

    private SelectVoucherAdapter adapter;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SelectVoucherPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectVoucherBinding.inflate(inflater, container, false);
        adapter = new SelectVoucherAdapter(this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        navController = NavHostFragment.findNavController(this);
        binding.voucherRecyclerview.setAdapter(adapter);
        presenter.getVoucher();
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    @Override
    public void getVouchers(List<Voucher> vouchers) {
        adapter.submitList(vouchers);
    }

    @Override
    public void isVouchersEmpty() {
        binding.setIsVouchersEmpty(true);
    }

    @Override
    public void onItemClick(Voucher voucher) {
        presenter.setSelectedVoucher(voucher);
    }

    public void onOKButtonClick() {
        navController.getPreviousBackStackEntry()
                        .getSavedStateHandle().set(Constant.VOUCHER_KEY_NAME, presenter.getSelectedVoucher());
        EventBus.getDefault().postSticky(new MessageVoucherSelected(presenter.getSelectedVoucher()));
        onNavigateUp();
    }

    @Override
    public void enableOkButton() {
        binding.selectVoucherButton.setVisibility(View.VISIBLE);
    }
}