package com.example.loginapp.model.interator;

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

    private final DatabaseReference favoriteProductRef = Constant.favoriteProductRef;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final ProductListener listener;

    public ProductInterator(ProductListener listener) {
        this.listener = listener;
    }

    public void isFavoriteProduct(Product product) {
        favoriteProductRef.child(currentUser.getUid())
                .child(String.valueOf(product.getId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.isFavoriteProduct(task.getResult().exists());
                    } else {
                        listener.onMessage("Get Favorite Fail");
                    }
                });
    }

    public void saveFavoriteProduct(Product product) {
        String productId = String.valueOf(product.getId());
        favoriteProductRef.child(currentUser.getUid()).child(productId).setValue(product)
                .addOnCompleteListener(s -> {
                    listener.isFavoriteProduct(true);
                    listener.hasNewFavoriteProduct();
                })
                .addOnFailureListener(e -> listener.onMessage("An error occurred. Please try again later"));
    }

    public void removeFavoriteProduct(int productID) {
        favoriteProductRef.child(currentUser.getUid()).child(String.valueOf(productID)).removeValue()
                .addOnCompleteListener(task -> listener.isFavoriteProduct(false))
                .addOnFailureListener(e -> listener.onMessage("An error occurred. Please try again later"));
    }

    public void getComments() {
        Call<CommentRespond> call = AppApiService.retrofit.getComments(5);
        call.enqueue(new Callback<CommentRespond>() {
            @Override
            public void onResponse(@NonNull Call<CommentRespond> call, @NonNull Response<CommentRespond> response) {
                if (response.isSuccessful()) {
                    CommentRespond commentRespond = response.body();
                    if (commentRespond != null) {
                        listener.getCommentRespond(commentRespond);
                    } else {
                        listener.onMessage("Empty");
                    }
                } else {
                    listener.onMessage("Respond fail");
                }
            }

            @Override
            public void onFailure(Call<CommentRespond> call, Throwable t) {
                listener.onMessage(t.getMessage());
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
                    if (productResponse != null)
                        listener.getSimilarProducts(productResponse.getProducts());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }
}
