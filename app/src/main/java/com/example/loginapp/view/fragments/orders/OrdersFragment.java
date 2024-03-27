package com.example.loginapp.view.fragments.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.adapter.order_pager_adapter.OrdersPagerAdapter;
import com.example.loginapp.databinding.FragmentOrdersBinding;
import com.example.loginapp.presenter.OrdersPresenter;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

public class OrdersFragment extends Fragment implements OrdersView {

    private static final String TAG = OrdersFragment.class.getSimpleName();

    private OrdersPresenter presenter;

    private FragmentOrdersBinding binding;

    private ViewPager2 viewPager2;

    private TabLayout tabLayout;

    private OrdersPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OrdersPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        adapter = new OrdersPagerAdapter(this);
        viewPager2 = binding.pager;
        tabLayout = binding.tabLayout;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(5);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addOrdersValueEventListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeOrdersValueEventListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewPager2 != null) {
            viewPager2.setAdapter(null);
            viewPager2 = null;
        }
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(null);
            tabLayout = null;
        }
        adapter = null;
        binding = null;
        viewPager2 = null;
    }

    public void onNavigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
        OrdersMessage message = EventBus.getDefault().getStickyEvent(OrdersMessage.class);
        if (message != null) EventBus.getDefault().removeStickyEvent(message);
    }

    @Override
    public void isLoading(Boolean loading) {

    }
}