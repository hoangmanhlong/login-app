package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.HomeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInterator {

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final DatabaseReference userRef =
        FirebaseDatabase.getInstance().getReference().child("users");

    private final HomeListener listener;

    public HomeInterator(HomeListener listener) {
        this.listener = listener;
    }

    public void getListProductFromNetwork() {
        listener.showProcessBar(true);
        Call<ProductResponse> call = AppApiService.retrofit.getProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        listener.onLoadProducts(productResponse.getProducts());
                    } else {
                        listener.onLoadError("Load data fail");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void getCategories() {
        Call<List<String>> call = AppApiService.retrofit.getCategories();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> categories = response.body();
                    if (categories != null) {
                        categories.add(0, "Recommended");
                        listener.onLoadCategories(categories);

                    } else {
                        listener.onLoadError("No Category item");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, Throwable t) {
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void getProductOfCategory(String category) {
        listener.showProcessBar(true);
        Call<ProductResponse> call = AppApiService.retrofit.getProductOfCategory(category);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        Log.d(this.toString(), productResponse.getProducts().toString());
                        listener.onLoadProducts(productResponse.getProducts());
                        listener.showProcessBar(false);
                    } else {
                        listener.onLoadError("Load data fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void getUserData() {
        assert currentUser != null;
        userRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserData user = snapshot.getValue(UserData.class);
                    listener.getUserData(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}
