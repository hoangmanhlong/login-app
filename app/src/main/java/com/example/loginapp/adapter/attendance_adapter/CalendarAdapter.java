package com.example.loginapp.adapter.attendance_adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutGetCoinBinding;

public class CalendarAdapter extends ListAdapter<String, CalendarAdapter.ItemViewHolder> {

    public CalendarAdapter() {
        super(DiffCallback);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutGetCoinBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.binding.setDay(getItem(position));
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final LayoutGetCoinBinding binding;

        public ItemViewHolder(LayoutGetCoinBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String day) {
            binding.setDay(day);
        }
    }

    public static DiffUtil.ItemCallback<String> DiffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return false;
        }
    };
}
