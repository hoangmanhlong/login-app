package com.example.loginapp.adapter.discount_adapter;

import com.example.loginapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DiscountDataSource {
    public static final List<DiscountItem> sliderItems = new ArrayList<>(
        Arrays.asList(
            new DiscountItem("iphone 9 discount 10%", R.drawable.iphone9),
            new DiscountItem("OPPO F19", R.drawable.oppo_f19),
            new DiscountItem("Samsung Galaxy Book", R.drawable.samsung)
        )
    );
}
