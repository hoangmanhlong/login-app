package com.example.loginapp.presenter;

import com.example.loginapp.data.local.AssertReader;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressDetailInteractor;
import com.example.loginapp.model.listener.DeliveryAddressDetailListener;
import com.example.loginapp.view.fragments.delivery_address_detail.DeliveryAddressDetailView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class DeliveryAddressDetailPresenter implements DeliveryAddressDetailListener {

    private final DeliveryAddressDetailInteractor interactor = new DeliveryAddressDetailInteractor(this);

    private boolean isNameValid, isPhoneNumberValid, isAddressValid, isProvinceValid, isPostalCodeValid = false;

    private final DeliveryAddressDetailView view;

    private final ExecutorService execute = Executors.newFixedThreadPool(5);

    private DeliveryAddress deliveryAddress;

    public DeliveryAddressDetailPresenter(DeliveryAddressDetailView view) {
        this.view = view;
    }

    public void createNewDeliveryAddress() {
        deliveryAddress = new DeliveryAddress();
        view.bindAddress(deliveryAddress);
    }

    public void fetchProvinces() {
        execute.execute(() -> {
            String[] provinces = AssertReader.getProvinces();
            if (provinces != null) view.bindProvinces(provinces);
            execute.shutdown();
        });
    }

    public void onSaveButtonClick(boolean isSaveDefaultDeliveryAddress) {
        deliveryAddress.setDefault(isSaveDefaultDeliveryAddress);
        interactor.updateDeliveryAddress(deliveryAddress);
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = new DeliveryAddress().copy(deliveryAddress);
        view.bindAddress(this.deliveryAddress);
    }

    public void deleteDeliveryAddress() {
        view.isLoading(true);
        interactor.deleteDeliveryAddress(deliveryAddress.getDeliveryAddressId());
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

    private boolean isAllInputValid() {
        return isNameValid && isPhoneNumberValid && isAddressValid && isProvinceValid && isPostalCodeValid;
    }

    @Override
    public void isLoading(Boolean loading) {
        view.isLoading(loading);
    }

    @Override
    public void deleteSuccess() {
        view.isLoading(false);
        view.navigateUp();
    }

    @Override
    public void isUpdateSuccess(Boolean success) {
        view.isLoading(false);
        if (success) view.navigateUp();
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
