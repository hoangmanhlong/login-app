package com.example.loginapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.loginapp.data.DataSource;
import com.example.loginapp.databinding.LayoutItemSliderviewBinding;
import com.example.loginapp.databinding.LayoutItemSliderviewBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private final List<SliderItem> mSliderItems = DataSource.sliderItems;

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SliderAdapterViewHolder(
            LayoutItemSliderviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        viewHolder.bind(mSliderItems.get(position));
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        private LayoutItemSliderviewBinding binding;

        public SliderAdapterViewHolder(LayoutItemSliderviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SliderItem sliderItem) {
            binding.setSliderItem(sliderItem);
        }
    }
}