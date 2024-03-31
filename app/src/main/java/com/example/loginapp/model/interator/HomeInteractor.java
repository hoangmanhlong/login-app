package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractor {

    private final AppDatabase database;

    private final String TAG = this.toString();

    private ValueEventListener userDataValueEventListener;

    private String uid;

    private HomeListener listener;

    public void clear() {
        userDataValueEventListener = null;
        uid = null;
        listener = null;
    }

    public HomeInteractor(HomeListener listener) {
        this.listener = listener;
        database = App.getDatabase();
        uid = FirebaseAuth.getInstance().getUid();
        userDataValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (listener != null) listener.getUserData(snapshot.getValue(UserData.class));
                } else {
                    if (listener != null) listener.isUserDataEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
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
                        if (listener != null) listener.getProductsFromAPI(products);
//                        insertProductNames(products);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void addUserDataValueEventListener() {
        if (uid != null) {
            Constant.userRef.child(uid)
                    .addValueEventListener(userDataValueEventListener);
        }
    }

    public void removeUserDataValueEventListener() {
        if (uid != null) {
            Constant.userRef.child(uid)
                    .removeEventListener(userDataValueEventListener);
        }
    }

    public void getFavoriteProductFromFirebase() {

        if (uid != null)
            Constant.favoriteProductRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (listener != null) {
                        if (snapshot.exists()) {
                            List<Product> products = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                products.add(dataSnapshot.getValue(Product.class));
                            listener.getFavoriteProducts(products);
                        } else {
                            listener.isFavoriteProductEmpty();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void insertProductNames(List<Product> products) {
        // Tạo một CountDownLatch với số lượng công việc là số lượng sản phẩm
        CountDownLatch latch = new CountDownLatch(products.size());

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ProductName> productNameList = new ArrayList<>();
            for (Product product : products)
                productNameList.add(new ProductName(product.getTitle()));
            database.dao().insertProduct(productNameList);

            // Giảm đếm của CountDownLatch khi công việc được hoàn thành
            latch.countDown();

            // Kiểm tra nếu tất cả công việc đã hoàn thành, hủy executor
            try {
                latch.await(); // Chờ cho tất cả các công việc hoàn thành
                AppDatabase.databaseWriteExecutor.shutdown(); // Hủy executor
            } catch (InterruptedException e) {
                Log.e(TAG, "insertProductNames: " +  e.getMessage());
            }
        });
    }

}