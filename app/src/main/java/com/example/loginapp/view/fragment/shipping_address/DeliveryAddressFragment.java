package com.example.loginapp.view.fragment.shipping_address;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.adapter.DeliveryAddressAdapter.DeliveryAddressAdapter;
import com.example.loginapp.adapter.DeliveryAddressAdapter.OnDeliveryAddressClickListener;
import com.example.loginapp.data.Constant;
import com.example.loginapp.databinding.FragmentDeliveryAddressBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.presenter.DeliveryAddressPresenter;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.LoadingDialog;

import java.util.List;

public class DeliveryAddressFragment extends Fragment implements OnDeliveryAddressClickListener, DeliveryAddressView  {

    private final DeliveryAddressPresenter presenter = new DeliveryAddressPresenter(this);

    private FragmentDeliveryAddressBinding binding;

    private final DeliveryAddressAdapter deliveryAddressAdapter = new DeliveryAddressAdapter(this);

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDeliveryAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        binding.deliveryAddressRecyclerview.setAdapter(deliveryAddressAdapter);
        presenter.initData();
    }

    @Override
    public void onDeliveryAddressClick(DeliveryAddress deliveryAddress) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DELIVERY_ADDRESS_KEY, deliveryAddress);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_deliveryAddressFragment_to_deliveryAddressDetailFragment, bundle);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void getShippingAddresses(List<DeliveryAddress> deliveryAddresses) {
        isLoading(false);
        deliveryAddressAdapter.submitList(deliveryAddresses);
        deliveryAddressAdapter.notifyItemInserted(deliveryAddresses.size() - 1);
    }

    @Override
    public void isLoading(boolean loading) {
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    public void onAddNewAddress() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_deliveryAddressFragment_to_deliveryAddressDetailFragment);
    }

    @Override
    public void isAddNewAddressButtonEnabled(Boolean enable) {
        if (enable) binding.addNewAddressButton.setVisibility(View.VISIBLE);
        else binding.addNewAddressButton.setVisibility(View.GONE);
    }

    @Override
    public void notifyItemChanged(int index) {
        deliveryAddressAdapter.notifyItemChanged(index);
    }

    @Override
    public void notifyItemRemoved(int index) {
        deliveryAddressAdapter.notifyItemRemoved(index);
    }
}