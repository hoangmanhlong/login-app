package com.example.loginapp.adapter.select_voucher_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.databinding.LayoutSelectVoucherBinding;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;

import java.util.ArrayList;
import java.util.List;

public class SelectVoucherAdapter extends ListAdapter<Voucher, SelectVoucherAdapter.SelectVoucherViewHolder> {

    private final OnSelectVoucherClickListener listener;

    private List<SelectVoucherViewHolder> viewHolderList = new ArrayList<>();


    public SelectVoucherAdapter(OnSelectVoucherClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectVoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectVoucherViewHolder viewHolder = new SelectVoucherViewHolder(LayoutSelectVoucherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
        viewHolderList.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectVoucherViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class SelectVoucherViewHolder extends RecyclerView.ViewHolder {

        private final LayoutSelectVoucherBinding binding;

        private final OnSelectVoucherClickListener listener;

        public SelectVoucherViewHolder(LayoutSelectVoucherBinding binding, OnSelectVoucherClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;

            binding.getRoot().setOnClickListener(v -> {
                listener.onItemClick(getItem(getAdapterPosition()));
                binding.radioButton.setChecked(true);
                listener.enableOkButton();
            });

            binding.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        listener.onItemClick(getItem(getAdapterPosition()));
                        listener.enableOkButton();
                        for (SelectVoucherViewHolder viewHolder : viewHolderList) {
                            if (viewHolder.getAdapterPosition() != getAdapterPosition()) {
                                viewHolder.binding.radioButton.setChecked(false);
                            }
                        }
                    }
                }
            });
        }

        @SuppressLint("ResourceAsColor")
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