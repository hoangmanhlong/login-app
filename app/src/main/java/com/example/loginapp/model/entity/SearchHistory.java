package com.example.loginapp.model.entity;

public class SearchHistory {

    private String text;

    private Long time;

    private boolean hasProductFound;

    private String category;

    public SearchHistory() {}

    public SearchHistory(String text) {
        this.text = text;
        time = System.currentTimeMillis();
    }

    public boolean isHasProductFound() {
        return hasProductFound;
    }

    public String getCategory() {
        return category;
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

    public static String CATEGORY_NAME = "category";
}
