package com.example.loginapp.presenter;

import android.util.Log;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.interator.CheckoutInfoInterator;
import com.example.loginapp.model.listener.CheckoutInfoListener;
import com.example.loginapp.view.fragments.checkout.CheckoutInfoView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CheckoutInfoPresenter implements CheckoutInfoListener {

    private static final String TAG = CheckoutInfoPresenter.class.getSimpleName();

    private final CheckoutInfoInterator interator = new CheckoutInfoInterator(this);

    private final CheckoutInfoView view;

    private boolean deliveryAddressesTaken = false;

    private Order currentOrder;

    private boolean retrievedTheSharedData = false;

    private DeliveryAddress deliveryAddress = new DeliveryAddress();

    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    private boolean isNameValid, isPhoneNumberValid, isAddressValid, isProvinceValid, isPostalCodeValid = false;

    public CheckoutInfoPresenter(CheckoutInfoView view) {
        this.view = view;
    }

    public List<DeliveryAddress> getDeliveryAddresses() {
        Log.d(TAG, "getDeliveryAddresses: when navigate" + deliveryAddresses);
        return deliveryAddresses;
    }

    public void initData() {
        if (deliveryAddressesTaken) {
            view.isSaveAddressCheckboxVisible(isSaveAddressCheckboxVisible());
        } else {
            interator.getDeliveryAddresses();
        }
        if (retrievedTheSharedData) {
            view.isLoading(false);
            view.isCheckoutButtonVisible(isAllInputValid());
            view.bindDeliveryAddress(deliveryAddress);
        } else {
            view.isCheckoutButtonVisible(false);
            view.getSharedData();
        }
    }

    public void setName(String name) {
        isNameValid = !name.isEmpty();
        deliveryAddress.setRecipientName(name);
        view.isCheckoutButtonVisible(isAllInputValid());
    }

    public void setPhoneNumber(String phoneNumber) {
        isPhoneNumberValid = phoneNumber.length() == 10 && isNumber(phoneNumber);
        deliveryAddress.setPhoneNumber(phoneNumber);
        view.isCheckoutButtonVisible(isAllInputValid());
    }

    public void setAddress(String address) {
        isAddressValid = !address.isEmpty();
        deliveryAddress.setAddress(address);
        view.isCheckoutButtonVisible(isAllInputValid());
    }

    public void setProvince(String province) {
        isProvinceValid = !province.isEmpty();
        deliveryAddress.setProvince(province);
        view.isCheckoutButtonVisible(isAllInputValid());
    }

    public void setPostalCode(String postalCode) {
        isPostalCodeValid = postalCode.length() == 6 && isNumber(postalCode);
        deliveryAddress.setPostalCode(postalCode);
        view.isCheckoutButtonVisible(isAllInputValid());
    }

    public void setShippingOption(String shippingOption) {
        deliveryAddress.setShippingOptions(shippingOption);
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        view.bindDeliveryAddress(deliveryAddress);
    }

    private boolean isAllInputValid() {
        return isNameValid && isPhoneNumberValid && isAddressValid && isProvinceValid && isPostalCodeValid;
    }

    public void setCurrentOrder(Order currentOrder) {
        retrievedTheSharedData = true;
        this.currentOrder = currentOrder;
    }

    @Override
    public void onMessage(String message) {

    }

    public void onCheckoutButtonClick() {
        currentOrder.setDeliveryAddress(deliveryAddress);
        view.navigateToPaymentMethodScreen(currentOrder);
    }

    @Override
    public void getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses) {
        view.isLoading(false);
        view.isSelectDeliveryAddressButtonVisible(true);
        this.deliveryAddresses = deliveryAddresses;
        view.isSaveAddressCheckboxVisible(isSaveAddressCheckboxVisible());
        Log.d(TAG, "getDeliveryAddresses: origin" + deliveryAddresses);
        List<DeliveryAddress> defaultDeliveryAddress = deliveryAddresses.stream().filter(DeliveryAddress::getIsDefault).collect(Collectors.toList());
        if (defaultDeliveryAddress.isEmpty()) deliveryAddress = deliveryAddresses.get(0);
        else deliveryAddress = defaultDeliveryAddress.get(0);
        view.bindDeliveryAddress(deliveryAddress);
        deliveryAddressesTaken = true;
    }

    private boolean isSaveAddressCheckboxVisible() {
        return deliveryAddresses.size() < 3;
    }

    @Override
    public void isDeliveryAddressesEmpty() {
        view.isLoading(false);
        view.isSelectDeliveryAddressButtonVisible(false);
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
