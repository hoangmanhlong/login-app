package com.example.loginapp.adapter.voucher_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.databinding.LayoutVoucherBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;

public class VoucherAdapter extends ListAdapter<Voucher, VoucherAdapter.VoucherViewHolder> {

    public VoucherAdapter() {
        super(DiffCallback);
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherViewHolder(LayoutVoucherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {

        private LayoutVoucherBinding binding;

        public VoucherViewHolder(LayoutVoucherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Voucher voucher) {
            binding.setVoucher(voucher);
            if (voucher.getVoucherType() == VoucherType.FreeShipping) {
                binding.voucherImageView.setBackgroundResource(R.color.free_shipping_color);
            }
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
