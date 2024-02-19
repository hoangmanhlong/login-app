package com.example.loginapp.view.fragments.cart;

import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.Product;

import java.util.List;

public interface CartView {

    void onMessage(String message);

    void bindProducts(List<FirebaseProduct> products);

    void notifyItemRemoved(int index);

    void notifyItemChanged(int index);

    void setTotal(String subtotal, String quantity, String total);

    void onItemClick(Product product);

    void setCheckAllChecked(boolean isChecked);

    void showCheckoutView(Boolean check);

    void showCheckAllCheckbox(Boolean visible);

    void isBasketEmpty(Boolean isEmpty);

    void isCheckAllCheckboxChecked(Boolean checked);

    void clearDiscountCode(Boolean clear);

    void bindDiscountCode(String code);
}
