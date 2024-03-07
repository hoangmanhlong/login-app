package com.example.loginapp.view.fragments.order_detail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.adapter.order_product_adapter.OrderProductAdapter;
import com.example.loginapp.databinding.FragmentOrderDetailBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.presenter.OrderDetailPresenter;
import com.example.loginapp.utils.Constant;

public class OrderDetailFragment extends Fragment implements OrderDetailView {

    private FragmentOrderDetailBinding binding;

    private final OrderDetailPresenter presenter = new OrderDetailPresenter(this);

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        binding.setFragment(this);
        getSharedOrder();
    }

    public void getSharedOrder() {
        Order order = getArguments() != null ? (Order) getArguments().getSerializable(Constant.ORDER_KEY) : null;
        if (order != null) presenter.setOrder(order);
    }

    public void onCancelOrderButtonClick() {
        new AlertDialog.Builder(requireContext())
                .setMessage(R.string.confirm_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> presenter.cancelOrder())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onBuyAgainButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, presenter.getOrder());
        navController
                .navigate(R.id.action_orderDetailFragment_to_buyAgainFragment, bundle);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    @Override
    public void bindOrder(Order order) {
        binding.setOrder(order);
        binding.setDeliveryAddress(order.getDeliveryAddress());
        boolean cancelOrderButtonVisible = true;
        int statusColorResId;
        switch (order.getOrderStatus()) {
            case Processing:
                statusColorResId = R.color.orange;
                break;
            case Completed:
                cancelOrderButtonVisible = false;
                statusColorResId = R.color.free_shipping_color;
                break;
            default:
                cancelOrderButtonVisible = false;
                statusColorResId = android.R.color.holo_red_dark;
                break;
        }

        binding.btCancelOrder.setVisibility(cancelOrderButtonVisible ? View.VISIBLE : View.GONE);
        binding.orderStatusView.setBackgroundResource(statusColorResId);
        binding.orderProductRecyclerview.setAdapter(new OrderProductAdapter(order.getOrderProducts()));
    }

    @Override
    public void backPreviousScreen() {
        onNavigateUp();
    }
}