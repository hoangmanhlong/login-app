package com.example.loginapp.view.fragment.cart;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface CartView {

    void onMessage(String message);

    void bindProducts(List<FirebaseProduct> products);

    void notifyItemRemoved(int index);

    void notifyItemChanged(int index);

    void isCheckAllVisibility(boolean visibility, boolean allChecked);

    void setTotal(String total, String quantity);

    void onDeleteProduct(int id);

    void onItemClick(Product product);

    void setCheckAllChecked(boolean isChecked);

    void showCheckoutView(Boolean check);

}