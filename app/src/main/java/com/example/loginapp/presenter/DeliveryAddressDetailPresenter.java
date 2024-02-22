package com.example.loginapp.presenter;

import android.os.AsyncTask;

import com.example.loginapp.data.local.AssertReader;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.interator.DeliveryAddressDetailInteractor;
import com.example.loginapp.model.listener.DeliveryAddressDetailListener;
import com.example.loginapp.view.fragments.delivery_address_detail.DeliveryAddressDetailView;

import java.lang.ref.WeakReference;

public class DeliveryAddressDetailPresenter implements DeliveryAddressDetailListener {

    private final DeliveryAddressDetailInteractor interactor = new DeliveryAddressDetailInteractor(this);


    private final DeliveryAddressDetailView view;

    private DeliveryAddress currentDeliveryAddress;

    public DeliveryAddressDetailPresenter(DeliveryAddressDetailView view) {
        this.view = view;
        new GetProvinceAsyncTask(this).execute();

    }

    private class GetProvinceAsyncTask extends AsyncTask<Void, Void, String[]> {

        private final WeakReference<DeliveryAddressDetailPresenter> presenterWeakReference;

        GetProvinceAsyncTask(DeliveryAddressDetailPresenter presenter) {
            presenterWeakReference = new WeakReference<>(presenter);
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            DeliveryAddressDetailPresenter presenter = presenterWeakReference.get();
            if (presenter == null) return null;
            return AssertReader.getProvinces();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            DeliveryAddressDetailPresenter presenter = presenterWeakReference.get();
            if (presenter != null && strings != null) {
                presenter.view.bindProvinces(strings); // Sử dụng đối số truyền vào hàm này thay vì gọi lại AssertReader.getProvinces()
            }
        }
    }

    public void setCurrentDeliveryAddress(DeliveryAddress deliveryAddress) {
        currentDeliveryAddress = deliveryAddress;
        view.bindAddress(currentDeliveryAddress);
    }

    public void deleteDeliveryAddress() {
        view.isLoading(true);
        interactor.deleteDeliveryAddress(currentDeliveryAddress.getDeliveryAddressId());
    }

    public void updateDeliveryAddress(String name, String phoneNumber, String address, String province, String postalCode, String country, String shippingOptions, Boolean isDefault) {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                currentDeliveryAddress != null ? currentDeliveryAddress.getDeliveryAddressId() : ("DA" + System.currentTimeMillis()),
                name,
                phoneNumber,
                address,
                province,
                Integer.parseInt(postalCode),
                country,
                shippingOptions.isEmpty() ? "" : shippingOptions,
                isDefault
        );
        interactor.updateDeliveryAddress(currentDeliveryAddress == null, deliveryAddress);
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
    public void onMessage(String message) {
        view.onMessage(message);
    }

    @Override
    public void isUpdateSuccess(Boolean success) {
        if (success) {
            view.isLoading(false);
            view.navigateUp();
        }
    }
}
