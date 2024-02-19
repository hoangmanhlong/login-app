package com.example.loginapp.model.entity;

import androidx.annotation.NonNull;

public class SearchHistory {

    private String text;

    private Long time;

    public SearchHistory() {}

    public SearchHistory(String text) {
        this.text = text;
        time = System.currentTimeMillis();
    }

    public Long getTime() {
        return time;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
