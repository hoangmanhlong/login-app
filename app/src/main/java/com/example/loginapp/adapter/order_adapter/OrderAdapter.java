package com.example.loginapp.adapter.order_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutOrderBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;

import java.util.List;

public class OrderAdapter extends ListAdapter<Order, OrderAdapter.OrderViewHolder> {

    private final OnOrderClickListener listener;

    public OrderAdapter(OnOrderClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOrderBinding binding;

        private final OnOrderClickListener listener;

        public OrderViewHolder(LayoutOrderBinding binding, OnOrderClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            binding.getRoot().setOnClickListener(v -> listener.onOrderClick(getItem(getAdapterPosition())));
        }

        public void bind(Order order) {
            List<OrderProduct> orderProducts = order.getOrderProducts();
            if (orderProducts.size() < 2) binding.viewMoreProduct.setVisibility(View.GONE);
            binding.setOrder(order);
            binding.setOrderProduct(order.getOrderProducts().get(0));
        }
    }

    private static final DiffUtil.ItemCallback<Order> DiffCallback = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getOrderId().equals(newItem.getOrderId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.equals(newItem);
        }
    };
}
