package com.example.loginapp.adapter.select_address_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutSelectedDeliveryAddressBinding;
import com.example.loginapp.model.entity.DeliveryAddress;

import java.util.ArrayList;
import java.util.List;

public class SelectSelectDeliveryAddressAdapter extends ListAdapter<DeliveryAddress, SelectSelectDeliveryAddressAdapter.SelectAddressViewHolder> {

    private final OnSelectDeliveryAddressClickListener listener;

    private List<SelectAddressViewHolder> viewHolders = new ArrayList<>();

    public SelectSelectDeliveryAddressAdapter(OnSelectDeliveryAddressClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectAddressViewHolder viewHolder = new SelectAddressViewHolder(
                LayoutSelectedDeliveryAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                listener
        );
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAddressViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public class SelectAddressViewHolder extends RecyclerView.ViewHolder {
        private final LayoutSelectedDeliveryAddressBinding binding;

        public SelectAddressViewHolder(LayoutSelectedDeliveryAddressBinding binding, OnSelectDeliveryAddressClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.onDeliveryAddressClick(getItem(getAdapterPosition()));
                binding.radioButton.setChecked(true);
                listener.enableOkButton();
            });

            binding.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        listener.onDeliveryAddressClick(getItem(getAdapterPosition()));
                        listener.enableOkButton();
                        for (SelectAddressViewHolder viewHolder : viewHolders) {
                            if (viewHolder.getAdapterPosition() != getAdapterPosition()) {
                                viewHolder.binding.radioButton.setChecked(false);
                            }
                        }
                    }
                }
            });
        }

        public void bind(DeliveryAddress deliveryAddress) {
            binding.setDeliveryAddress(deliveryAddress);
        }
    }

    private static final DiffUtil.ItemCallback<DeliveryAddress> DiffCallback = new DiffUtil.ItemCallback<DeliveryAddress>() {
        @Override
        public boolean areItemsTheSame(@NonNull DeliveryAddress oldItem, @NonNull DeliveryAddress newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DeliveryAddress oldItem, @NonNull DeliveryAddress newItem) {
            return false;
        }
    };
}
