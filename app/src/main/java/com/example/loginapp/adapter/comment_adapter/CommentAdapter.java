package com.example.loginapp.adapter.comment_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutCommentBinding;
import com.example.loginapp.model.entity.Comment;

public class CommentAdapter extends ListAdapter<Comment, CommentAdapter.CommentViewHolder> {

    public CommentAdapter() {
        super(DiffCallback);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private final LayoutCommentBinding binding;

        public CommentViewHolder(LayoutCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Comment comment) {
            binding.setComment(comment);
        }
    }

    private static final DiffUtil.ItemCallback<Comment> DiffCallback = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.equals(newItem);
        }
    };
}
