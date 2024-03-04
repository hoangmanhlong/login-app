package com.example.loginapp.view.fragments.select_delivery_address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.adapter.select_address_adapter.OnSelectDeliveryAddressClickListener;
import com.example.loginapp.adapter.select_address_adapter.SelectSelectDeliveryAddressAdapter;
import com.example.loginapp.databinding.FragmentSelectDeliveryAddressBinding;
import com.example.loginapp.model.entity.Date;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.DeliveryAddresses;
import com.example.loginapp.presenter.SelectDeliveryAddressPresenter;
import com.example.loginapp.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class SelectDeliveryAddressFragment extends Fragment implements OnSelectDeliveryAddressClickListener, SelectDeliveryAddressView {

    private final SelectDeliveryAddressPresenter presenter = new SelectDeliveryAddressPresenter(this);

    private FragmentSelectDeliveryAddressBinding binding;

    private final SelectSelectDeliveryAddressAdapter adapter = new SelectSelectDeliveryAddressAdapter(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectDeliveryAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        getDataShared();
    }

    private void getDataShared() {
        if (getArguments() != null) {
            DeliveryAddresses deliveryAddresses = (DeliveryAddresses) getArguments().getSerializable(Constant.DELIVERY_ADDRESSES_KEY);
            binding.deliveryAddressRecyclerview.setAdapter(adapter);
            adapter.submitList(deliveryAddresses.deliveryAddresses);
        }
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void onOKButtonClick() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
        EventBus.getDefault().postSticky(new SelectedDeliveryAddressMessage(presenter.selectedAddress));
    }

    @Override
    public void onDeliveryAddressClick(DeliveryAddress deliveryAddress) {
        presenter.selectedAddress = deliveryAddress;
    }

    @Override
    public void enableOkButton() {
        binding.selectVoucherButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        adapter.submitList(deliveryAddresses);
    }
}