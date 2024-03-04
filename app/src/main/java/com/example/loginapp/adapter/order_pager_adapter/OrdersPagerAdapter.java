package com.example.loginapp.adapter.order_pager_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loginapp.view.fragments.orders.AllOrdersFragment;
import com.example.loginapp.view.fragments.orders.CancelOrdersFragment;
import com.example.loginapp.view.fragments.orders.CompletedOrdersFragment;
import com.example.loginapp.view.fragments.orders.ProcessingOrdersFragment;
import com.example.loginapp.view.fragments.orders.ReturnOrdersFragment;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    public OrdersPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ProcessingOrdersFragment();
            case 2:
                return new CompletedOrdersFragment();
            case 3:
                return new ReturnOrdersFragment();
            case 4:
                return new CancelOrdersFragment();
            default:
                return new AllOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
