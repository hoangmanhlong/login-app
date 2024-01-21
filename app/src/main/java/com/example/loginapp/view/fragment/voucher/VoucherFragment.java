package com.example.loginapp.view.fragment.voucher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
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

        LinearLayout bottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        RecyclerView recyclerView = binding.voucherRecyclerview;
        recyclerView.setAdapter(adapter);
        adapter.submitList(presenter.vouchers);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void notifyVoucherAdded(List<Voucher> vouchers) {
        Log.d(this.toString(), "notifyVoucherAdded: " + vouchers.size());
        adapter.submitList(vouchers);
        adapter.notifyItemInserted(vouchers.size() - 1);
    }
}