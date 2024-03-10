package com.example.loginapp.view.fragments.orders;

import android.os.Bundle;
import android.util.Log;
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
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.presenter.OrdersPresenter;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class OrdersFragment extends Fragment implements OrdersView {

    private final String TAG = this.toString();

    private final OrdersPresenter presenter = new OrdersPresenter(this);

    private FragmentOrdersBinding binding;

    private ViewPager2 viewPager2;

    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "OrdersFragment onViewCreated: ");
        binding.setFragment(this);
        viewPager2 = binding.pager;
        OrdersPagerAdapter adapter = new OrdersPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(5);

        tabLayout = binding.tabLayout;

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
        
        presenter.initData();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    public void onNavigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void getOrders(List<Order> orders) {
        Log.d(TAG, "getOrders: " + orders.size());
        EventBus.getDefault().postSticky(new OrdersMessage(orders));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OrdersMessage message = EventBus.getDefault().getStickyEvent(OrdersMessage.class);
        if (message != null) EventBus.getDefault().removeStickyEvent(message);
    }



    @Override
    public void isLoading(Boolean loading) {

    }
}