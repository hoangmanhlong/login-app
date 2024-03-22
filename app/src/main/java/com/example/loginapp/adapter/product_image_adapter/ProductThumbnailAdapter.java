package com.example.loginapp.adapter.product_image_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.loginapp.databinding.LayoutProductThumbnailBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductThumbnailAdapter extends SliderViewAdapter<ProductThumbnailAdapter.ProductThumbnailViewHolder> {

    private List<String> list = new ArrayList<>();

    public void setData(List<String> strings) {
        list = strings;
        notifyDataSetChanged();
    }

    @Override
    public ProductThumbnailViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ProductThumbnailViewHolder(LayoutProductThumbnailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ProductThumbnailViewHolder viewHolder, int position) {
        viewHolder.bind(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public static class ProductThumbnailViewHolder extends SliderViewAdapter.ViewHolder {

        private final LayoutProductThumbnailBinding binding;

        public ProductThumbnailViewHolder(LayoutProductThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String imageUrl) {
            binding.setImageUrl(imageUrl);
        }
    }
}
