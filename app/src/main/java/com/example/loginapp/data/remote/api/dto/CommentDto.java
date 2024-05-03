package com.example.loginapp.data.remote.api.dto;

import com.example.loginapp.model.entity.Comment;

import java.util.List;

public class CommentDto {

    private List<Comment> comments;

    private int total;

    private int skip;

    private int limit;

    public CommentDto(List<Comment> comments, int total, int skip, int limit) {
        this.comments = comments;
        this.total = total;
        this.skip = skip;
        this.limit = limit;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getTotal() {
        return total;
    }

    public int getSkip() {
        return skip;
    }

    public int getLimit() {
        return limit;
    }
}
