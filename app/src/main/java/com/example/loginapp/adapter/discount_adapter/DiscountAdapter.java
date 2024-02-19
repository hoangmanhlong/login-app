package com.example.loginapp.adapter.discount_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.loginapp.databinding.LayoutDiscountItemBinding;
import com.example.loginapp.model.entity.Product;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class DiscountAdapter extends SliderViewAdapter<DiscountAdapter.DiscountSliderAdapterViewHolder> {

    private final List<Product> products;

    public DiscountAdapter(List<Product> products) {
        this.products = products;
    }

    public void setData(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
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
        viewHolder.bind(products.get(position));
    }

    @Override
    public int getCount() {
        return products.size();
    }

    static class DiscountSliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

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