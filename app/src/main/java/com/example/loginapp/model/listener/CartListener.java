package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;

public interface CartListener {

    void notifyItemAdded(FirebaseProduct product);

    void notifyItemChanged(FirebaseProduct product);

    void onMessage(String message);

    void notifyItemRemoved(FirebaseProduct product);

    void isCartEmpty();
}
