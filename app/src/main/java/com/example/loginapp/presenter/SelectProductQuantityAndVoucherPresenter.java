package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.bottom_sheet.SelectProductQuantityAndVoucherView;

import java.util.Collections;

public class SelectProductQuantityAndVoucherPresenter {

    private SelectProductQuantityAndVoucherView view;

    private int quantity = 1;

    private Product product;

    private Order order;

    public SelectProductQuantityAndVoucherPresenter(SelectProductQuantityAndVoucherView view) {
        this.view = view;
        order = new Order();
    }

    public void clear() {
        view = null;
        product = null;
        order = null;
    }

    public void setProduct(Product product) {
        this.product = product;
        view.bindProduct(product);
        view.isSelectVoucherViewVisible(true);
        updateUI();
    }

    public void onCheckoutButtonClick() {
        order.setOrderProducts(Collections.singletonList(product.toOrderProduct(quantity)));
        view.navigateToCheckoutInfoFragment(order);
    }

    public void onPlusBtnClick() {
        quantity++;
        updateUI();
    }

    public void clearVoucher() {
        order.setVoucher(null);
        view.isSelectVoucherViewVisible(true);
        view.bindDiscountCode("");
        updateUI();
    }

    public void setVoucher(Voucher voucher) {
        order.setVoucher(voucher);
        view.bindDiscountCode(voucher.getVoucherCode());
        view.isSelectVoucherViewVisible(false);
        updateUI();
    }

    public void onMinusBtnClick() {
        if (quantity > 1) {
            quantity--;
            updateUI();
        }
    }

    private void updateUI() {
        double total = product.getPrice() * quantity;
        Voucher voucher = order.getVoucher();
        if (voucher != null) {
            if (voucher.getVoucherType() == VoucherType.Discount)
                total = (total - ((total * voucher.getDiscountPercentage()) / 100));
            if (voucher.getVoucherType() == VoucherType.FreeShipping)
                total -= Constant.ShippingCost;
        }
        total = Math.round(total * 100.00) / 100.00;
        view.bindQuantityAndTotal(String.valueOf(quantity), String.valueOf(total));
    }
}
