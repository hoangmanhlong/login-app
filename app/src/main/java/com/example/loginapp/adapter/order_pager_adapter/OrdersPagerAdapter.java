package com.example.loginapp.adapter.order_pager_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loginapp.view.fragments.orders.AllOrdersFragment;
import com.example.loginapp.view.fragments.orders.CompletedOrdersFragment;
import com.example.loginapp.view.fragments.orders.ProcessingOrdersFragment;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    public OrdersPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllOrdersFragment();
            case 1:
                return new ProcessingOrdersFragment();
            case 2:
                return new CompletedOrdersFragment();
            case 3:
            case 4:
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
