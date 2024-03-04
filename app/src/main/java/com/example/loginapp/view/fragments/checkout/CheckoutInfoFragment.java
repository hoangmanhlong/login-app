package com.example.loginapp.view.fragments.checkout;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.model.entity.DeliveryAddresses;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.databinding.FragmentCheckoutInfoBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.presenter.CheckoutInfoPresenter;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.fragments.select_delivery_address.SelectedDeliveryAddressMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CheckoutInfoFragment extends Fragment implements CheckoutInfoView {

    private final String TAG = this.toString();

    private final CheckoutInfoPresenter presenter = new CheckoutInfoPresenter(this);

    private FragmentCheckoutInfoBinding binding;

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        presenter.setCurrentOrder((Order) getArguments().getSerializable(Constant.ORDER_KEY));
        presenter.initData();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getDeliveryAddress(SelectedDeliveryAddressMessage message) {
        DeliveryAddress deliveryAddress = message.getDeliveryAddress();
        presenter.setSelectedDeliveryAddress(deliveryAddress);
        Log.d(TAG, "getDeliveryAddress: " + deliveryAddress);
        updateUI(deliveryAddress);
    }

    private void updateUI(DeliveryAddress deliveryAddress) {
        binding.setDeliveryAddress(deliveryAddress);
        binding.saveAddressCheckBox.setChecked(false);
        binding.saveAddressView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SelectedDeliveryAddressMessage message = EventBus.getDefault().getStickyEvent(SelectedDeliveryAddressMessage.class);
        if (message != null) EventBus.getDefault().removeStickyEvent(message);
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void onPaymentOptionScreen() {
        String name = binding.etName.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();
        String province = binding.etProvince.getText().toString().trim();
        String postalCode = binding.etPostalCode.getText().toString().trim();
        String country = binding.etCountry.getText().toString().trim();
        String shoppingOption = binding.etShoppingOption.getText().toString().trim();
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        DeliveryAddress deliveryAddress = presenter.checkInput(name,phoneNumber, address, province, postalCode, country, shoppingOption);
        if (deliveryAddress == null) {
            onMessage("Please enter complete information");
        } else {
            Bundle bundle = new Bundle();
            Order order1;
            Order order = presenter.getCurrentOrder();
            if (order.getVoucher() != null) order1 = new Order(order.getOrderProducts(), deliveryAddress, order.getVoucher());
            else order1 = new Order(order.getOrderProducts(), deliveryAddress);
            bundle.putSerializable(Constant.ORDER_KEY, order1);
            bundle.putBoolean(Constant.SAVE_ADDRESS_KEY, binding.saveAddressCheckBox.isChecked());
            bundle.putBoolean(Constant.IS_CART, getArguments().getBoolean(Constant.IS_CART));
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_checkoutInfoFragment_to_paymentOptionFragment, bundle);
        }
    }

    @Override
    public void isLoading(boolean isLoading) {
        if (isLoading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void bindDefaultDeliveryAddress(DeliveryAddress deliveryAddress) {
        updateUI(deliveryAddress);
    }

    public void goSelectDeliveryAddressScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DELIVERY_ADDRESSES_KEY, new DeliveryAddresses(presenter.getDeliveryAddresses()));
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_checkoutInfoFragment_to_selectDeliveryAddressFragment, bundle);
    }
}
