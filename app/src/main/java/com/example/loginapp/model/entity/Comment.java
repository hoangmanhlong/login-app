package com.example.loginapp.model.entity;

public class Comment {
    private int id;

    private String body;

    private String postId;

    private User user;

    public Comment(int id, String body, String postId, User user) {
        this.id = id;
        this.body = body;
        this.postId = postId;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getPostId() {
        return postId;
    }

    public User getUser() {
        return user;
    }
}
