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
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.adapter.order_adapter.OnOrderClickListener;
import com.example.loginapp.adapter.order_adapter.OrdersAdapter;
import com.example.loginapp.databinding.FragmentOrdersAllBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class AllOrdersFragment extends Fragment implements OnOrderClickListener {

    private FragmentOrdersAllBinding binding;

    private OrdersAdapter ordersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersAllBinding.inflate(inflater, container, false);
        ordersAdapter = new OrdersAdapter(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(this.toString(), "AllOrdersFragment onViewCreated: ");
        RecyclerView orderRecyclerview = binding.ordersRecyclerView;
        orderRecyclerview.setAdapter(ordersAdapter);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getOrders(OrdersMessage message) {
        List<Order> orders = message.orders;
        binding.setIsOrdersListEmpty(orders.isEmpty());
        if (!orders.isEmpty())
            ordersAdapter.submitList(
                    message.orders.stream()
                            .sorted(Comparator.comparing(Order::getOrderId).reversed())
                            .collect(Collectors.toList())
            );
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        ordersAdapter = null;
    }

    @Override
    public void onOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, order);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_ordersFragment_to_orderDetailFragment, bundle);
    }
}