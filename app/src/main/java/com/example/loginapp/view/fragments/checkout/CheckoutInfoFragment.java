package com.example.loginapp.view.fragments.checkout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentCheckoutInfoBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.DeliveryAddresses;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.presenter.CheckoutInfoPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.fragments.select_delivery_address.SelectedDeliveryAddressMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CheckoutInfoFragment extends Fragment implements CheckoutInfoView {

    private static final String TAG = CheckoutInfoFragment.class.getSimpleName();

    private NavController navController;

    private CheckoutInfoPresenter presenter;

    private FragmentCheckoutInfoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new CheckoutInfoPresenter(this);
    }

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
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        binding.setFragment(this);
        presenter.initData();
        editTextListener();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getDeliveryAddress(SelectedDeliveryAddressMessage message) {
        DeliveryAddress deliveryAddress = message.getDeliveryAddress();
        if (deliveryAddress != null) presenter.setDeliveryAddress(deliveryAddress);
        SelectedDeliveryAddressMessage selectedDeliveryAddressMessage = EventBus.getDefault().getStickyEvent(SelectedDeliveryAddressMessage.class);
        if (selectedDeliveryAddressMessage != null) EventBus.getDefault().removeStickyEvent(selectedDeliveryAddressMessage);
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void onCheckoutButtonClick() {
        presenter.onCheckoutButtonClick();
    }

    @Override
    public void isLoading(boolean isLoading) {
        if (isLoading) binding.processIndicator.show();
        else binding.processIndicator.hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void bindDeliveryAddress(DeliveryAddress deliveryAddress) {
        binding.setDeliveryAddress(deliveryAddress);
        binding.saveAddressCheckBox.setChecked(false);
    }

    @Override
    public void getSharedData() {
        if (getArguments() != null) {
            Order order = (Order) getArguments().getSerializable(Constant.ORDER_KEY);
            if (order != null) presenter.setCurrentOrder(order);
        }
    }

    @Override
    public void isCheckoutButtonVisible(boolean visible) {
        binding.btCheckout.setEnabled(visible);
    }

    @Override
    public void isSelectDeliveryAddressButtonVisible(boolean visible) {
        binding.btSelectDeliveryAddress.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void navigateToPaymentMethodScreen(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.ORDER_KEY, order);
        bundle.putBoolean(Constant.SAVE_ADDRESS_KEY, binding.saveAddressCheckBox.isChecked());
        bundle.putBoolean(Constant.IS_PRODUCTS_FROM_CART, getArguments().getBoolean(Constant.IS_PRODUCTS_FROM_CART));
        navController.navigate(R.id.action_checkoutInfoFragment_to_paymentOptionFragment, bundle);
    }

    @Override
    public void isSaveAddressCheckboxVisible(boolean visible) {
        binding.saveAddressCheckBox.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void goSelectDeliveryAddressScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DELIVERY_ADDRESSES_KEY, new DeliveryAddresses(presenter.getDeliveryAddresses()));
        navController.navigate(R.id.action_checkoutInfoFragment_to_selectDeliveryAddressFragment, bundle);
    }

    private void editTextListener() {
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setName(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPhoneNumber(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setAddress(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setProvince(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPostalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPostalCode(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etShippingOption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setShippingOption(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
