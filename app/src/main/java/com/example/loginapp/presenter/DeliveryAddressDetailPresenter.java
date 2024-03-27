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

    private DeliveryAddressDetailInteractor interactor;

    private boolean isNameValid, isPhoneNumberValid, isAddressValid, isProvinceValid, isPostalCodeValid = false;

    private DeliveryAddressDetailView view;

    private ExecutorService execute;

    private DeliveryAddress deliveryAddress;

    public DeliveryAddressDetailPresenter(DeliveryAddressDetailView view) {
        this.view = view;
        deliveryAddress = new DeliveryAddress();
        interactor = new DeliveryAddressDetailInteractor(this);
        execute = Executors.newFixedThreadPool(5);
    }

    public void fetchProvinces() {
        execute.execute(() -> {
            String[] provinces = AssertReader.getProvinces();
            if (provinces != null && view != null) view.bindProvinces(provinces);
            execute.shutdown();
        });
    }

    public void onSaveButtonClick(boolean isSaveDefaultDeliveryAddress) {
        deliveryAddress.setDefault(isSaveDefaultDeliveryAddress);
        interactor.updateDeliveryAddress(deliveryAddress);
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress.copy(deliveryAddress);
        view.bindAddress(this.deliveryAddress);
    }

    public void deleteDeliveryAddress() {
        view.isLoading(true);
        interactor.deleteDeliveryAddress(deliveryAddress.getDeliveryAddressId());
    }

    public void setName(String name) {
        isNameValid = !name.isEmpty();
        deliveryAddress.setRecipientName(name);
        view.isCheckoutButtonEnabled(isAllInputValid());
    }

    public void setPhoneNumber(String phoneNumber) {
        isPhoneNumberValid = phoneNumber.length() == 10 && isNumber(phoneNumber);
        deliveryAddress.setPhoneNumber(phoneNumber);
        view.isCheckoutButtonEnabled(isAllInputValid());
    }

    public void setAddress(String address) {
        isAddressValid = !address.isEmpty();
        deliveryAddress.setAddress(address);
        view.isCheckoutButtonEnabled(isAllInputValid());
    }

    public void setProvince(String province) {
        isProvinceValid = !province.isEmpty();
        deliveryAddress.setProvince(province);
        view.isCheckoutButtonEnabled(isAllInputValid());
    }

    public void setPostalCode(String postalCode) {
        isPostalCodeValid = postalCode.length() == 6 && isNumber(postalCode);
        deliveryAddress.setPostalCode(postalCode);
        view.isCheckoutButtonEnabled(isAllInputValid());
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

    public void detachView() {
        view = null;
        interactor.clearData();
        interactor = null;
        execute = null;
        deliveryAddress = null;
    }
}
