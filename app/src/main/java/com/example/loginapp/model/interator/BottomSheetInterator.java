package com.example.loginapp.model.interator;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.BottomSheetListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetInterator {

    private final BottomSheetListener listener;

    public BottomSheetInterator(BottomSheetListener listener) {
        this.listener = listener;
    }

    public void getProduct(int id) {
        Call<Product> call = AppApiService.retrofit.getProduct(id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) {
                        listener.onLoadProduct(product);
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
    }
}
