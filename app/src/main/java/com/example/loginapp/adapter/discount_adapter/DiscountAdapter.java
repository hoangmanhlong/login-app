package com.example.loginapp.adapter.discount_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.loginapp.adapter.product_adapter.OnProductClickListener;
import com.example.loginapp.databinding.LayoutDiscountItemBinding;
import com.example.loginapp.model.entity.Product;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends SliderViewAdapter<DiscountAdapter.DiscountSliderAdapterViewHolder> {

    private final List<Product> products;

    private final OnProductClickListener listener;

    public DiscountAdapter(OnProductClickListener listener) {
        this.products = new ArrayList<>();
        this.listener = listener;
    }

    public void setData(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public DiscountSliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        return new DiscountSliderAdapterViewHolder(
            LayoutDiscountItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(DiscountSliderAdapterViewHolder viewHolder, final int position) {
        Product product = products.get(position);
        viewHolder.itemView.setOnClickListener(v -> listener.onItemClick(product));
        viewHolder.bind(product);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    public static class DiscountSliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        private final LayoutDiscountItemBinding binding;

        public DiscountSliderAdapterViewHolder(LayoutDiscountItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.setProduct(product);
        }
    }
}