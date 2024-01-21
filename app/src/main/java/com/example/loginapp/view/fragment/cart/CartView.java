package com.example.loginapp.view.fragment.cart;

import com.example.loginapp.model.entity.FirebaseProduct;

import java.util.List;

public interface CartView {

    void onMessage(String message);

    void notifyItemAdded(List<FirebaseProduct> products);

    void notifyItemRemoved(int index);

    void notifyItemChanged(int index);

    void isCheckAllVisibility(boolean visibility, boolean allChecked);

    void setTotal(String total, String quantity);

    void onDeleteProduct(int id);

    void onItemClick(int id);

    void setCheckAllChecked(boolean isChecked);

    void showCheckoutView(Boolean check);

}