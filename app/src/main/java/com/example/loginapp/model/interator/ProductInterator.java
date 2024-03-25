package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInterator {

    private static final String TAG = ProductInterator.class.getSimpleName();

    private DatabaseReference favoriteProductRef = Constant.favoriteProductRef;

    private FirebaseUser currentUser;

    private ProductListener listener;

    public ProductInterator(ProductListener listener) {
        this.listener = listener;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void clear() {
        listener = null;
        favoriteProductRef = null;
        currentUser = null;
    }

    public void isFavoriteProduct(Product product) {
        favoriteProductRef.child(currentUser.getUid())
                .child(String.valueOf(product.getId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) listener.isFavoriteProduct(task.getResult().exists());
                    } else {
                        Log.e(TAG, "isFavoriteProduct: Get Favorite Fail");
                    }
                    getComments();
                    getSimilarProducts(product.getCategory());
                });
    }

    public void saveFavoriteProduct(Product product) {
        String productId = String.valueOf(product.getId());
        favoriteProductRef.child(currentUser.getUid()).child(productId).setValue(product)
                .addOnCompleteListener(s -> {
                    if (listener != null) {
                        listener.isFavoriteProduct(true);
                        listener.hasNewFavoriteProduct();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "removeFavoriteProduct: " + e.getMessage()));
    }

    public void removeFavoriteProduct(int productID) {
        favoriteProductRef.child(currentUser.getUid()).child(String.valueOf(productID)).removeValue()
                .addOnCompleteListener(task -> listener.isFavoriteProduct(false))
                .addOnFailureListener(e -> Log.e(TAG, "removeFavoriteProduct: " + e.getMessage()));
    }

    public void getComments() {
        Call<CommentRespond> call = AppApiService.retrofit.getComments(5);
        call.enqueue(new Callback<CommentRespond>() {
            @Override
            public void onResponse(@NonNull Call<CommentRespond> call, @NonNull Response<CommentRespond> response) {
                if (response.isSuccessful()) {
                    CommentRespond commentRespond = response.body();
                    if (commentRespond != null && listener != null)
                        listener.getCommentRespond(commentRespond);
                }
            }

            @Override
            public void onFailure(Call<CommentRespond> call, Throwable t) {

            }
        });
    }

    public void getSimilarProducts(String category) {
        Call<ProductResponse> call = AppApiService.retrofit.getProductOfCategory(category);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null && listener != null)
                        listener.getSimilarProducts(productResponse.getProducts());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "  + t.getMessage());
            }
        });
    }
}
