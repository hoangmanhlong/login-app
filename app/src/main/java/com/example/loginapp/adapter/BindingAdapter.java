package com.example.loginapp.adapter;

import android.widget.ImageView;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("imgRes")
    public static void loadImage(ImageView view, int res){
        view.setImageResource(res);
    }
}