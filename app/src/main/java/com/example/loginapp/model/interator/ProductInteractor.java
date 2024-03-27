package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.ProductListener;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInteractor {

    private static final String TAG = ProductInteractor.class.getSimpleName();

    private DatabaseReference favoriteProductRef = Constant.favoriteProductRef;

    private String uid;

    private ProductListener listener;

    public ProductInteractor(ProductListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void clear() {
        listener = null;
        favoriteProductRef = null;
        uid = null;
    }

    public void isFavoriteProduct(Product product) {
        favoriteProductRef.child(uid)
                .child(String.valueOf(product.getId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && listener != null) {
                        listener.isFavoriteProduct(task.getResult().exists());
                        getComments();
                        getSimilarProducts(product.getCategory());
                    }
                });
    }

    public void saveFavoriteProduct(Product product) {
        String productId = String.valueOf(product.getId());
        favoriteProductRef.child(uid).child(productId).setValue(product)
                .addOnCompleteListener(s -> {
                    if (listener != null) {
                        listener.isFavoriteProduct(true);
                        EventBus.getDefault().postSticky(new NewProductInWishlistMessage());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "removeFavoriteProduct: " + e.getMessage()));
    }

    public void removeFavoriteProduct(int productID) {
        favoriteProductRef.child(uid).child(String.valueOf(productID)).removeValue()
                .addOnCompleteListener(task -> {if (listener != null) listener.isFavoriteProduct(false);})
                .addOnFailureListener(e -> Log.e(TAG, "removeFavoriteProduct: " + e.getMessage()));
    }

    private void getComments() {
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
            public void onFailure(@NonNull Call<CommentRespond> call, @NonNull Throwable t) {

            }
        });
    }

    private void getSimilarProducts(String category) {
        Call<ProductResponse> call = AppApiService.retrofit.getProductOfCategory(category);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null && listener != null)
                        listener.getSimilarProducts(productResponse.getProducts());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
