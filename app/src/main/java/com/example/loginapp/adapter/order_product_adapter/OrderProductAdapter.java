package com.example.loginapp.adapter.order_product_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutOrderProductBinding;
import com.example.loginapp.model.entity.OrderProduct;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    private final List<OrderProduct> orderProducts;

    public OrderProductAdapter(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderProductViewHolder(LayoutOrderProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        holder.bind(orderProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return orderProducts.size();
    }

    public static class OrderProductViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOrderProductBinding binding;

        public OrderProductViewHolder(LayoutOrderProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderProduct orderProduct) {
            binding.setOrderProduct(orderProduct);
        }
    }
}
