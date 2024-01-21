package com.example.loginapp.adapter.favorite_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.databinding.LayoutFavoriteBinding;


public class FavoriteAdapter extends
    ListAdapter<Product, FavoriteAdapter.ItemViewHolder> {

    private final FavoriteItemClickListener onItemClickListener;


    public FavoriteAdapter(FavoriteItemClickListener listener) {
        super(DiffCallback);
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteAdapter.ItemViewHolder(LayoutFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(
        FavoriteAdapter.ItemViewHolder holder,
        int position
    ) {
        holder.bind(getItem(position));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final LayoutFavoriteBinding binding;

        private FavoriteItemClickListener onItemClickListener;

        public ItemViewHolder(LayoutFavoriteBinding binding, FavoriteItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(Product product) {
            binding.setProduct(product);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == binding.getRoot().getId())
                onItemClickListener.onItemClick(getItem(getAdapterPosition()));
        }
    }

    public static final DiffUtil.ItemCallback<Product> DiffCallback =
        new DiffUtil.ItemCallback<Product>() {
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