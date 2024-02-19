package com.example.loginapp.adapter.favorite_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutFavoriteBinding;
import com.example.loginapp.model.entity.Product;


public class FavoriteAdapter extends ListAdapter<Product, FavoriteAdapter.ItemViewHolder> {

    private final FavoriteItemClickListener listener;

    public FavoriteAdapter(FavoriteItemClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    public void getProductByPosition(int pos) {
        listener.getProductByPosition(getItem(pos).getId());
    }

    @NonNull
    @Override
    public FavoriteAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteAdapter.ItemViewHolder(LayoutFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final LayoutFavoriteBinding binding;

        private final FavoriteItemClickListener listener;

        public ItemViewHolder(LayoutFavoriteBinding binding, FavoriteItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = onItemClickListener;
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(getItem(getAdapterPosition())));
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