package com.example.loginapp.adapter.main_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loginapp.view.fragments.cart.CartFragment;
import com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment;
import com.example.loginapp.view.fragments.home.HomeFragment;
import com.example.loginapp.view.fragments.search.SearchProductFragment;
import com.example.loginapp.view.fragments.user_profile.UserProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new SearchProductFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new FavoriteProductFragment();
            case 4:
                return new UserProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
