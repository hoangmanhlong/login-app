package com.example.loginapp.adapter.voucher_viewPager_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loginapp.view.fragments.voucher.AllVouchersFragment;
import com.example.loginapp.view.fragments.voucher.MyVouchersFragment;

public class VoucherViewPagerAdapter extends FragmentStateAdapter {

    public VoucherViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 1 ? new MyVouchersFragment() : new AllVouchersFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
