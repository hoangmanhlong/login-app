package com.example.loginapp.adapter;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class SliderItem {
    @DrawableRes
    private int image;
    @StringRes
    private int title;
    @StringRes
    private int description;

    public SliderItem(int item, int title, int description) {
        this.image = item;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public int getTitle() {
        return title;
    }

    public int getDescription() {
        return description;
    }
}
