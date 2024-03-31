package com.example.loginapp.model.interator;

import android.util.Log;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.listener.AddProductToCartListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AddProductToCartInteractor {

    private static final String TAG = AddProductToCartInteractor.class.getSimpleName();

    private AddProductToCartListener listener;

    public AddProductToCartInteractor(AddProductToCartListener listener) {
        this.listener = listener;
    }

    public void clear() {
        listener = null;
    }

    public void addProductToCart(FirebaseProduct product) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference cartRef = Constant.cartRef.child(uid).child(String.valueOf(product.getId()));
            cartRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseProduct product1 = task.getResult().getValue(FirebaseProduct.class);
                    if (product1 == null)
                        cartRef.setValue(product)
                                .addOnSuccessListener(unused -> {
                                    if(listener != null) listener.isAddProductSuccess(true);
                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, "addProductToCart: " + e.getMessage());
                                    if(listener != null) listener.isAddProductSuccess(false);
                                });
                    else
                        cartRef.child(FirebaseProduct.QUANTITY).setValue(product1.getQuantity() + product.getQuantity())
                                .addOnSuccessListener(unused -> {
                                    if(listener != null) listener.isAddProductSuccess(true);
                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, "addProductToCart: " + e.getMessage());
                                    if(listener != null) listener.isAddProductSuccess(false);
                                });
                }
            });
        }

    }
}
