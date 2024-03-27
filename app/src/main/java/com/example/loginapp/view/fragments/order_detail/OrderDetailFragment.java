package com.example.loginapp.view.fragments.order_detail;

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
import com.example.loginapp.view.commonUI.AppConfirmDialog;

public class OrderDetailFragment extends Fragment implements OrderDetailView {

    private FragmentOrderDetailBinding binding;

    private OrderDetailPresenter presenter;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OrderDetailPresenter(this);
        navController = NavHostFragment.findNavController(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initData();
    }

    @Override
    public void getSharedOrder() {
        if (getArguments() != null) {
            Order order = (Order) getArguments().getSerializable(Constant.ORDER_KEY);
            if (order != null) presenter.setOrder(order);
        }
    }

    public void onCancelOrderButtonClick() {
        AppConfirmDialog.show(
                requireContext(),
                getString(R.string.delete),
                getString(R.string.confirm_message),
                new AppConfirmDialog.AppConfirmDialogButtonListener() {
                    @Override
                    public void onPositiveButtonClickListener() {
                        presenter.cancelOrder();
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                }
        );
    }

    public void onBuyAgainButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, presenter.getOrder());
        navController.navigate(R.id.action_orderDetailFragment_to_buyAgainFragment, bundle);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
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