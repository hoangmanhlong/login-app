package com.example.loginapp.adapter.attendance_adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutGetCoinBinding;
import com.example.loginapp.model.entity.DayWithCheck;

public class CalendarAdapter extends ListAdapter<DayWithCheck, CalendarAdapter.ItemViewHolder> {

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
        holder.bind(getItem(position));
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final LayoutGetCoinBinding binding;

        public ItemViewHolder(LayoutGetCoinBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DayWithCheck day) {
            binding.setDayWithCheck(day);
            binding.executePendingBindings();
        }
    }

    public static DiffUtil.ItemCallback<DayWithCheck> DiffCallback = new DiffUtil.ItemCallback<DayWithCheck>() {
        @Override
        public boolean areItemsTheSame(@NonNull DayWithCheck oldItem, @NonNull DayWithCheck newItem) {
            return oldItem.getDay() == newItem.getDay();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull DayWithCheck oldItem, @NonNull DayWithCheck newItem) {
            return oldItem.equals(newItem);
        }
    };
}
