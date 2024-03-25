package com.example.loginapp.view.fragments.bottom_sheet;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.Product;

public interface SelectProductQuantityAndVoucherView {
    void navigateToCheckoutInfoFragment(Order order);

    void isSelectVoucherViewVisible(boolean visible);

    void bindQuantityAndTotal(String quantity, String total);

    void bindProduct(Product product);

    void bindDiscountCode(String discountCode);
}
