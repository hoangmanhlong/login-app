package com.example.loginapp.adapter.DeliveryAddressAdapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutAddressBinding;
import com.example.loginapp.model.entity.DeliveryAddress;

public class DeliveryAddressAdapter extends ListAdapter<DeliveryAddress, DeliveryAddressAdapter.DeliveryAddressViewHolder> {

    private final OnDeliveryAddressClickListener listener;


    public DeliveryAddressAdapter(OnDeliveryAddressClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeliveryAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeliveryAddressViewHolder(LayoutAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(DeliveryAddressViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class DeliveryAddressViewHolder extends RecyclerView.ViewHolder {
        private final LayoutAddressBinding binding;

        public DeliveryAddressViewHolder(LayoutAddressBinding binding, OnDeliveryAddressClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onDeliveryAddressClick(getItem(getAdapterPosition())));
        }

        public void bind(DeliveryAddress deliveryAddress) {
            binding.setDeliveryAddress(deliveryAddress);
            binding.executePendingBindings();
        }
    }

    public static final DiffUtil.ItemCallback<DeliveryAddress> DiffCallback = new DiffUtil.ItemCallback<DeliveryAddress>() {
        @Override
        public boolean areItemsTheSame(@NonNull DeliveryAddress oldItem, @NonNull DeliveryAddress newItem) {
            return oldItem.getDeliveryAddressId().equals(newItem.getDeliveryAddressId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull DeliveryAddress oldItem, @NonNull DeliveryAddress newItem) {
            return oldItem.equals(newItem);
        }
    };

}
