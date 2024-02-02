package com.example.loginapp.adapter.expand_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutProductLinearBinding;
import com.example.loginapp.model.entity.Product;
import com.google.protobuf.Internal;

import java.util.List;

public class ExpandAdapter extends ListAdapter<Product, ExpandAdapter.ItemViewHolder> {

    private final ExpandProductClickListener listener;

    public ExpandAdapter(ExpandProductClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutProductLinearBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final LayoutProductLinearBinding binding;

        public ItemViewHolder(LayoutProductLinearBinding binding, ExpandProductClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onProductClick(getItem(getAdapterPosition())));
        }

        public void bind(Product product) {
            binding.setProduct(product);
        }
    }

    private static final DiffUtil.ItemCallback<Product> DiffCallback = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };
}
