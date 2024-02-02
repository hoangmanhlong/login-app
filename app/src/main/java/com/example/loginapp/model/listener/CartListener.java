package com.example.loginapp.model.listener;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Voucher;

public interface CartListener {

    void notifyItemAdded(FirebaseProduct product, String previousChildName);

    void notifyItemRemoved(FirebaseProduct product);

    void notifyItemChanged(FirebaseProduct product, String previousChildName);

    void onMessage(String message);
}
