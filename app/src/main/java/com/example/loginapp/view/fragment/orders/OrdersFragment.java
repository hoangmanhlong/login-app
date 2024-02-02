package com.example.loginapp.view.fragment.orders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginapp.R;
import com.example.loginapp.adapter.order_adapter.OnOrderClickListener;
import com.example.loginapp.adapter.order_adapter.OrderAdapter;
import com.example.loginapp.data.Constant;
import com.example.loginapp.databinding.FragmentOrdersBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.presenter.OrderPresenter;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.LoadingDialog;

import java.util.List;

public class OrdersFragment extends Fragment implements OrderView, OnOrderClickListener {

    private final OrderPresenter presenter = new OrderPresenter(this);

    private FragmentOrdersBinding binding;

    private final OrderAdapter orderAdapter = new OrderAdapter(this);

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());

        RecyclerView orderRecyclerview  = binding.orderRecyclerview;
        orderRecyclerview.setAdapter(orderAdapter);
        presenter.initData();
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void getOrders(List<Order> orders) {
        orderAdapter.submitList(orders);
        orderAdapter.notifyItemInserted(orders.size() - 1);
    }

    @Override
    public void isLoading(Boolean loading) {
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void onOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, order);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_ordersFragment_to_orderDetailFragment,bundle);
    }
}