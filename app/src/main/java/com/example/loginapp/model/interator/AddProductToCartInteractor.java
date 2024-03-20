package com.example.loginapp.model.interator;

import android.util.Log;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.listener.AddProductToCartListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AddProductToCartInteractor {

    private static final String TAG = AddProductToCartInteractor.class.getSimpleName();

    private final AddProductToCartListener listener;

    public AddProductToCartInteractor(AddProductToCartListener listener) {
        this.listener = listener;
    }

    public void addProductToCart(FirebaseProduct product) {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference cartRef = Constant.cartRef.child(uid).child(String.valueOf(product.getId()));
        cartRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseProduct product1 = task.getResult().getValue(FirebaseProduct.class);
                if (product1 == null)
                    cartRef.setValue(product)
                            .addOnSuccessListener(unused -> listener.isAddProductSuccess(true))
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "addProductToCart: " + e.getMessage());
                                listener.isAddProductSuccess(false);
                            });
                else
                    cartRef.child(FirebaseProduct.QUANTITY).setValue(product1.getQuantity() + product.getQuantity())
                            .addOnSuccessListener(unused -> listener.isAddProductSuccess(true))
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "addProductToCart: " + e.getMessage());
                                listener.isAddProductSuccess(false);
                            });
            }
        });
    }
}
