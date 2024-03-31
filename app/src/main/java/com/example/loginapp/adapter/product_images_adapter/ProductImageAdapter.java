package com.example.loginapp.adapter.product_images_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutProductImageSmailBinding;

/**
 * This Adapter using in [ProductFragment]
 */
public class ProductImageAdapter extends ListAdapter<String, ProductImageAdapter.ImageViewHolder> {

    private final OnImageClickListener listener;

    public ProductImageAdapter(OnImageClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutProductImageSmailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private final LayoutProductImageSmailBinding binding;

        public ImageViewHolder(LayoutProductImageSmailBinding binding, OnImageClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onImageClick(getAdapterPosition()));
        }

        public void bind(String imgUrl) {
            binding.setImgUrl(imgUrl);
        }
    }

    private static final DiffUtil.ItemCallback<String> DiffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };
}
