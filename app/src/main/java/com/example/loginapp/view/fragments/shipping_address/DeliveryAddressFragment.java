package com.example.loginapp.view.fragments.shipping_address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.loginapp.R;
import com.example.loginapp.adapter.DeliveryAddressAdapter.DeliveryAddressAdapter;
import com.example.loginapp.adapter.DeliveryAddressAdapter.OnDeliveryAddressClickListener;
import com.example.loginapp.databinding.FragmentDeliveryAddressBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.presenter.DeliveryAddressPresenter;
import com.example.loginapp.utils.Constant;

import java.util.List;

public class DeliveryAddressFragment extends Fragment implements OnDeliveryAddressClickListener, DeliveryAddressView {

    private DeliveryAddressPresenter presenter;

    private FragmentDeliveryAddressBinding binding;

    private DeliveryAddressAdapter deliveryAddressAdapter;

    private NavController navController;

    RecyclerView deliveryAddressesRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new DeliveryAddressPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDeliveryAddressBinding.inflate(inflater, container, false);
        deliveryAddressesRecyclerView = binding.deliveryAddressRecyclerview;
        deliveryAddressAdapter = new DeliveryAddressAdapter(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        deliveryAddressesRecyclerView.setAdapter(deliveryAddressAdapter);
        SimpleItemAnimator simpleItemAnimator = ((SimpleItemAnimator) deliveryAddressesRecyclerView.getItemAnimator());
        if (simpleItemAnimator != null) simpleItemAnimator.setSupportsChangeAnimations(false);
        presenter.initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addDeliveryAddressValueEventListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeDeliveryAddressValueEventListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deliveryAddressAdapter = null;
        deliveryAddressesRecyclerView = null;
        binding = null;
    }

    @Override
    public void onDeliveryAddressClick(DeliveryAddress deliveryAddress) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DELIVERY_ADDRESS_KEY, deliveryAddress);
        navController.navigate(R.id.action_deliveryAddressFragment_to_deliveryAddressDetailFragment, bundle);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    @Override
    public void bindShippingAddresses(List<DeliveryAddress> deliveryAddresses) {
        deliveryAddressAdapter.submitList(deliveryAddresses);
    }

    @Override
    public void isLoading(boolean loading) {
        binding.setIsLoading(loading);
    }

    @Override
    public void isDeliveryAddressEmpty(boolean isEmpty) {
        binding.setIsDeliveryAddressEmpty(isEmpty);
    }

    @Override
    public void addNewDeliveryAddressButtonVisible(boolean visible) {
        binding.setAddNewDeliveryAddressButtonVisible(visible);
    }

    public void onAddNewAddress() {
        navController.navigate(R.id.action_deliveryAddressFragment_to_deliveryAddressDetailFragment);
    }
}