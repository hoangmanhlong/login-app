package com.example.loginapp.model.listener;

public interface DeliveryAddressDetailListener {
    void isLoading(Boolean loading);

    void deleteSuccess();

    void onMessage(String message);

    void isUpdateSuccess(Boolean success);
}
