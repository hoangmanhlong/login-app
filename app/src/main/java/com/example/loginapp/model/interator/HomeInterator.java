package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.App;
import com.example.loginapp.data.local.room.AppDatabase;
import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.ProductName;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.HomeListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInterator {

    private final AppDatabase database = App.getDatabase();

    private final String TAG = this.toString();

    @Nullable
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final HomeListener listener;

    public HomeInterator(HomeListener listener) {
        this.listener = listener;
    }

    public void getListProductFromNetwork() {
        Call<ProductResponse> call = AppApiService.retrofit.getProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        List<Product> products = productResponse.getProducts();
                        listener.getProductsFromAPI(products);
                        insertProductNames(products);
                    } else {
                        listener.onMessage("Load data fail");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUserData() {
        if (currentUser != null) {
            Constant.userRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listener.getUserData(snapshot.getValue(UserData.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getFavoriteProductFromFirebase() {
        List<Product> products = new ArrayList<>();
        if (currentUser != null)
            Constant.favoriteProductRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        products.add(dataSnapshot.getValue(Product.class));
                    listener.getFavoriteProducts(products);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

//    public void updateBestseller(List<Product> products) {
//        for (Product product : products)
//            Constant.bestSellerRef.child(String.valueOf(product.getId()))
//                    .setValue(product);
//    }

    public void getBestsellerProducts() {
        List<Product> products = new ArrayList<>();
        Constant.bestSellerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            products.add(dataSnapshot.getValue(Product.class));
                        listener.getBestsellerProducts(products);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void insertProductNames(List<Product> products) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ProductName> productNameList = new ArrayList<>();

            for (Product product : products)
                productNameList.add(new ProductName(product.getTitle()));
            database.dao().insertProduct(productNameList);
        });
    }
}