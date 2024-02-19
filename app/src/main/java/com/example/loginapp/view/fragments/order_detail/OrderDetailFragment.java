package com.example.loginapp.view.fragments.order_detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.adapter.order_product_adapter.OrderProductAdapter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.databinding.FragmentOrderDetailBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;

public class OrderDetailFragment extends Fragment {

    private FragmentOrderDetailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        binding.setFragment(this);
        Order order = (Order) getArguments().getSerializable(Constant.ORDER_KEY);
        binding.setOrder(order);
        binding.setDeliveryAddress(order.getDeliveryAddress());
        if (order.getOrderStatus() == OrderStatus.Completed)
            binding.orderStatusView.setBackgroundResource(R.color.free_shipping_color);
        if (order.getOrderStatus() == OrderStatus.Cancel)
            binding.orderStatusView.setBackgroundColor(Color.RED);
        if (order.getOrderStatus() == OrderStatus.Processing)
            binding.orderStatusView.setBackgroundResource(R.color.orange);


        OrderProductAdapter orderProductAdapter = new OrderProductAdapter(order.getOrderProducts());
        binding.orderProductRecyclerview.setAdapter(orderProductAdapter);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }
}