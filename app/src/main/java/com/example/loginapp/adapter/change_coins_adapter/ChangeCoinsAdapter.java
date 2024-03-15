package com.example.loginapp.adapter.change_coins_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.databinding.LayoutVoucherCoinExchangeBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;

public class ChangeCoinsAdapter extends ListAdapter<Voucher, ChangeCoinsAdapter.VoucherViewHolder> {

    private final OnVoucherClickListener listener;

    public ChangeCoinsAdapter(OnVoucherClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChangeCoinsAdapter.VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChangeCoinsAdapter.VoucherViewHolder(LayoutVoucherCoinExchangeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeCoinsAdapter.VoucherViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {

        private final LayoutVoucherCoinExchangeBinding binding;

        public VoucherViewHolder(LayoutVoucherCoinExchangeBinding binding, OnVoucherClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btRedeem.setOnClickListener(c -> listener.onVoucherClick(getItem(getAdapterPosition())));
        }

        public void bind(Voucher voucher) {
            binding.setVoucher(voucher);
            if (voucher.getVoucherType() == VoucherType.FreeShipping)
                binding.voucherImageView.setBackgroundResource(R.color.free_shipping_color);
            else binding.voucherImageView.setBackgroundResource(R.color.orange);
        }
    }

    public static final DiffUtil.ItemCallback<Voucher> DiffCallback = new DiffUtil.ItemCallback<Voucher>() {
        @Override
        public boolean areItemsTheSame(@NonNull Voucher oldItem, @NonNull Voucher newItem) {
            return oldItem.getVoucherCode().equals(newItem.getVoucherCode());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Voucher oldItem, @NonNull Voucher newItem) {
            return oldItem.equals(newItem);
        }
    };
}
