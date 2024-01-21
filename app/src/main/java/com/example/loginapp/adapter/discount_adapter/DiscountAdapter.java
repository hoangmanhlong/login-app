package com.example.loginapp.adapter.discount_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.loginapp.databinding.LayoutDiscountItemBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class DiscountAdapter extends
    SliderViewAdapter<DiscountAdapter.DiscountSliderAdapterViewHolder> {

    private final List<DiscountItem> mSliderItems = DiscountDataSource.sliderItems;

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
        viewHolder.bind(mSliderItems.get(position));
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class DiscountSliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        private LayoutDiscountItemBinding binding;

        public DiscountSliderAdapterViewHolder(LayoutDiscountItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DiscountItem discountItem) {
            binding.setDiscountItem(discountItem);
        }
    }
}