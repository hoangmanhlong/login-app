package com.example.loginapp.data;

import com.example.loginapp.R;
import com.example.loginapp.adapter.SliderItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DataSource {
    public static final List<SliderItem> sliderItems = new ArrayList<>(
        Arrays.asList(
            new SliderItem(R.drawable.option1, R.string.best_prices, R.string.find_your_f),
            new SliderItem(R.drawable.option2, R.string.track_your_, R.string.track_your),
            new SliderItem(R.drawable.option3, R.string.free_and_fa_, R.string.free_and_fa)
        )
    );
}
