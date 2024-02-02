package com.example.loginapp.adapter.seach_suggest_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutSearchSuggestionBinding;
import com.example.loginapp.model.entity.SearchHistory;

public class SearchSuggestAdapter extends ListAdapter<SearchHistory, SearchSuggestAdapter.ItemViewHolder> {
    private OnSearchSuggestionClickListener listener;

    public SearchSuggestAdapter(OnSearchSuggestionClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(listener, LayoutSearchSuggestionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position).getText());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutSearchSuggestionBinding binding;
        OnSearchSuggestionClickListener listener;

        public ItemViewHolder(OnSearchSuggestionClickListener listener, LayoutSearchSuggestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            binding.getRoot().setOnClickListener(this);
            binding.searchHistory.setOnClickListener(this);
            binding.searchHistory.setOnCloseIconClickListener(v -> listener.deleteSearchHistory(getItem(getAdapterPosition())));
        }

        public void bind(String str) {
            binding.searchHistory.setText(str);
        }

        @Override
        public void onClick(View v) {
            SearchHistory history = getItem(getAdapterPosition());
            if (v.getId() == binding.searchHistory.getId()) {
                listener.onSearchSuggestClick(history);
            }
        }
    }

    public static final DiffUtil.ItemCallback<SearchHistory> DiffCallback = new DiffUtil.ItemCallback<SearchHistory>() {
        @Override
        public boolean areItemsTheSame(@NonNull SearchHistory oldItem, @NonNull SearchHistory newItem) {
            return oldItem.getTime().equals(newItem.getTime());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull SearchHistory oldItem, @NonNull SearchHistory newItem) {
            return oldItem.equals(newItem);
        }
    };
}
