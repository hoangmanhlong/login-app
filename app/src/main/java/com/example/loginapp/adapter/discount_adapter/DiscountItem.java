package com.example.loginapp.adapter.discount_adapter;

import androidx.annotation.DrawableRes;

public class DiscountItem {
    private String title;
    @DrawableRes private int image;

    public DiscountItem(String title, @DrawableRes int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
