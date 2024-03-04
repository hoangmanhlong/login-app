package com.example.loginapp.view.fragments.buy_again;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.adapter.order_product_adapter.OrderProductAdapter;
import com.example.loginapp.databinding.FragmentBuyAgainBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.presenter.BuyAgainPresenter;
import com.example.loginapp.utils.Constant;

import java.util.List;


public class BuyAgainFragment extends Fragment implements BuyAgainView {

    private final BuyAgainPresenter presenter = new BuyAgainPresenter(this);

    private FragmentBuyAgainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBuyAgainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        getSharedData();
    }

    private void getSharedData() {
        if (getArguments() != null) {
            presenter.setCurrentOrder((Order) getArguments().getSerializable(Constant.ORDER_KEY));
        }
    }

    public void onNavigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    public void onSelectCodeClick() {

    }

    public void onClearDiscountCodeClick() {

    }

    public void onCheckoutClick() {

    }

    @Override
    public void bindAddress(DeliveryAddress address) {
        binding.setAddress(address);
    }

    @Override
    public void bindOrderProducts(List<OrderProduct> products) {
        OrderProductAdapter adapter = new OrderProductAdapter(products);
        binding.orderProductsRecyclerView.setAdapter(adapter);
    }
}