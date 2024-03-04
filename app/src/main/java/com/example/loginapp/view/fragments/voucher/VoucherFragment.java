package com.example.loginapp.view.fragments.voucher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.adapter.voucher_viewPager_adapter.VoucherViewPagerAdapter;
import com.example.loginapp.databinding.FragmentVoucherBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;


public class VoucherFragment extends Fragment {

    private FragmentVoucherBinding binding;

    private ViewPager2 viewPager;

    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVoucherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);

        tabLayout = binding.vouchersTabLayout;
        viewPager = binding.pager;

        VoucherViewPagerAdapter viewPagerAdapter = new VoucherViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }
        });

        BadgeDrawable badgeDrawable = tabLayout.getTabAt(1).getOrCreateBadge();

        badgeDrawable.setText("New");
        badgeDrawable.setVerticalOffset(20);
        badgeDrawable.setHorizontalOffset(20);
    }



    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }
}