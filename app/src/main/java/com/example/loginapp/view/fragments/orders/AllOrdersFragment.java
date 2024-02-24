package com.example.loginapp.view.fragments.orders;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.adapter.order_adapter.OnOrderClickListener;
import com.example.loginapp.adapter.order_adapter.OrdersAdapter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.databinding.FragmentOrdersAllBinding;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.presenter.AllOrdersPresenter;
import com.example.loginapp.view.commonUI.LoadingDialog;

import java.util.List;


public class AllOrdersFragment extends Fragment implements OnOrderClickListener, OrderView {

    private final AllOrdersPresenter presenter = new AllOrdersPresenter(this);

    private FragmentOrdersAllBinding binding;

    private final OrdersAdapter ordersAdapter = new OrdersAdapter(this);

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersAllBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        RecyclerView orderRecyclerview = binding.ordersRecyclerView;
        orderRecyclerview.setAdapter(ordersAdapter);
        presenter.initData();
    }

    @Override
    public void onOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, order);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_ordersFragment_to_orderDetailFragment, bundle);
    }

    @Override
    public void getOrders(List<Order> orders) {
        ordersAdapter.submitList(orders);
    }

    @Override
    public void isLoading(Boolean loading) {
//        if (loading) dialog.show();
//        else dialog.dismiss();
    }
}