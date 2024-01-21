package com.example.loginapp.model.interator;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.listener.ProductListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInterator {
    private final DatabaseReference favoriteProductRef = FirebaseDatabase.getInstance().getReference().child(Constant.FAVORITE_PRODUCT_REF);

    private final DatabaseReference cartRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.CART_REF);

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final ProductListener listener;

    public ProductInterator(ProductListener listener) {
        this.listener = listener;
    }

    public List<Product> products;

    public void getProduct(int id) {
        listener.isLoading(true);
        products = new ArrayList<>();
        Call<Product> call = AppApiService.retrofit.getProduct(id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) {
                        listener.onGetProduct(product);
                        products.add(product);
                        listener.isLoading(false);
                        if (products.size() == 2) {
                            compare(products.get(0), products.get(1));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                listener.onMessage(t.getMessage());
            }
        });

        assert currentUser != null;
        favoriteProductRef.child(currentUser.getUid()).child(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);
                    listener.onGetProduct(product);
                    if (products.size() == 2) {
                        compare(products.get(0), products.get(1));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void compare(Product apiProduct, Product firebaseProduct) {
        listener.enableFavorite(apiProduct != null && firebaseProduct != null && apiProduct.getId() == firebaseProduct.getId());
    }

    public void saveFavoriteProduct(Product product) {
        String productId = String.valueOf(product.getId());
        favoriteProductRef.child(currentUser.getUid()).child(productId).setValue(product)
            .addOnCompleteListener(s -> listener.enableFavorite(true))
            .addOnFailureListener(e -> listener.onMessage("An error occurred. Please try again later"));
    }

    public void removeFavoriteProduct(int productID) {
        favoriteProductRef.child(currentUser.getUid()).child(String.valueOf(productID)).removeValue()
            .addOnCompleteListener(task -> listener.removeSuccess());
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
                    cartRef
                        .child(currentUser.getUid()).child(id).child("quantity")
                        .setValue(String.valueOf(Integer.parseInt(product1.getQuantity()) + 1))
                        .addOnCompleteListener(a -> listener.saveToBasketSuccess())
                        .addOnFailureListener(e -> listener.onMessage("An error occurred. Please try again later"));
                }
            }
        });
    }
}
