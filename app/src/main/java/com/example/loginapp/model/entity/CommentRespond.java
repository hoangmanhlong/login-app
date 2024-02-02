package com.example.loginapp.model.entity;

import java.util.List;

public class CommentRespond {

    private List<Comment> comments;

    private int total;

    private int skip;

    private int limit;

    public CommentRespond(List<Comment> comments, int total, int skip, int limit) {
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
