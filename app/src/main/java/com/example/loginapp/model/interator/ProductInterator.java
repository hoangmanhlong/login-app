package com.example.loginapp.model.interator;

import com.example.loginapp.data.Constant;
import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.model.entity.CommentRespond;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.ProductListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInterator {

    private final DatabaseReference favoriteProductRef = Constant.favoriteProductRef;

    private final DatabaseReference cartRef = Constant.cartRef;

    private final FirebaseUser currentUser = Constant.currentUser;

    private final ProductListener listener;

    public ProductInterator(ProductListener listener) {
        this.listener = listener;
    }

    public void isFavoriteProduct(Product product) {
        favoriteProductRef
                .child(currentUser.getUid())
                .child(String.valueOf(product.getId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.isFavoriteProduct(task.getResult().getValue(Product.class) != null);
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

    public void updateQuantity(FirebaseProduct product) {
        String id = String.valueOf(product.getId());
        cartRef.child(currentUser.getUid()).child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseProduct product1 = task.getResult().getValue(FirebaseProduct.class);
                if (product1 == null) {
                    cartRef.child(currentUser.getUid()).child(id).setValue(product)
                            .addOnCompleteListener(a -> listener.saveToBasketSuccess())
                            .addOnFailureListener(e -> listener.onMessage("An error occurred. Please try again later"));
                } else {
                    cartRef.child(currentUser.getUid()).child(id).child("quantity")
                            .setValue(String.valueOf(Integer.parseInt(product1.getQuantity()) + 1))
                            .addOnCompleteListener(a -> listener.saveToBasketSuccess())
                            .addOnFailureListener(e -> listener.onMessage("An error occurred. Please try again later"));
                }
            }
        });
    }

    public void getComments() {
        Call<CommentRespond> call = AppApiService.retrofit.getComments(5);
        call.enqueue(new Callback<CommentRespond>() {
            @Override
            public void onResponse(Call<CommentRespond> call, Response<CommentRespond> response) {
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
}
