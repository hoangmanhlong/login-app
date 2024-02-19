package com.example.loginapp.adapter.order_adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.databinding.LayoutOrderBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.OrderStatus;

import java.util.List;

public class OrdersAdapter extends ListAdapter<Order, OrdersAdapter.OrderViewHolder> {

    private final OnOrderClickListener listener;

    public OrdersAdapter(OnOrderClickListener listener) {
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

        public OrderViewHolder(LayoutOrderBinding binding, OnOrderClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onOrderClick(getItem(getAdapterPosition())));
        }

        @SuppressLint("ResourceAsColor")
        public void bind(Order order) {
            List<OrderProduct> orderProducts = order.getOrderProducts();
            binding.setOrder(order);
            binding.setOrderProduct(order.getOrderProducts().get(0));
            if (orderProducts.size() < 2) binding.viewMoreProduct.setVisibility(View.GONE);
            if (order.getOrderStatus() == OrderStatus.Cancel) binding.tvOrderStatus.setTextColor(Color.RED);
            if (order.getOrderStatus() == OrderStatus.Completed) binding.tvOrderStatus.setTextColor(R.color.free_shipping_color);
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
