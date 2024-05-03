package com.example.loginapp.adapter.product_image_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.loginapp.R;

import java.util.ArrayList;
import java.util.List;

public class ImagesProductAdapter extends PagerAdapter {

    private List<String> images;

    private final Context context;

    public ImagesProductAdapter(Context context) {
        this.context = context;
        images = new ArrayList<>();
    }

    public void setImages(List<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_product_image_big, container, false);
        ImageView imageView = view.findViewById(R.id.product_image_big);
        Glide.with(context)
                .load(images.get(position))
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
