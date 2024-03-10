package com.example.loginapp.adapter.overview_slider_adapter;

import com.example.loginapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SliderDataSource {
    public static final List<SliderItem> sliderItems = new ArrayList<>(
        Arrays.asList(
            new SliderItem(R.drawable.option1, R.string.best_prices_and_deals, R.string.best_prices_and_deals_des),
            new SliderItem(R.drawable.option2, R.string.track_your_orders_title, R.string.track_your_orders_description),
            new SliderItem(R.drawable.option3, R.string.free_and_fast_title, R.string.free_and_fast_description)
        )
    );
}
