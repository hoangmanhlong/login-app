package com.example.loginapp.adapter.seach_suggest_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutFavoriteBinding;
import com.example.loginapp.model.entity.Product;

public class SearchProductAdapter extends ListAdapter<Product, SearchProductAdapter.ItemViewHolder> {

    private final OnSearchProductClickListener listener;

    public SearchProductAdapter(OnSearchProductClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(SearchProductAdapter.ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final LayoutFavoriteBinding binding;

        public ItemViewHolder(LayoutFavoriteBinding binding, OnSearchProductClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onProductClick(getItem(getAdapterPosition())));
        }

        public void bind(Product product) {
            binding.setProduct(product);
            binding.executePendingBindings();
        }
    }

    public static final DiffUtil.ItemCallback<Product> DiffCallback = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(Product oldItem, Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };
}
