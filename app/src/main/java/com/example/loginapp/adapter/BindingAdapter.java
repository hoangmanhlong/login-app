package com.example.loginapp.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.loginapp.R;

import java.util.Locale;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("imgRes")
    public static void loadImage(ImageView view, int res) {
        view.setImageResource(res);
    }

    @androidx.databinding.BindingAdapter("imgUrl")
    public static void loadImageUrl(ImageView view, String imgUrl) {
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(view.getContext())
                .load(imgUrl)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(view);
        } else {
            view.setImageResource(R.drawable.ic_user);
        }
    }

    @androidx.databinding.BindingAdapter("app:doubleToString")
    public static void doubleToString(TextView view, double value) {
        // Format the double value as needed
        String formattedValue = String.format(Locale.getDefault(), "%.1f", value);
        view.setText(formattedValue);
    }

    @androidx.databinding.BindingAdapter("intToString")
    public static void intToString(TextView textView, int value) {
        textView.setText(String.valueOf(value));
    }
}