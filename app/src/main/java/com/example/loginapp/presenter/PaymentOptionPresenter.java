package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;
import com.example.loginapp.model.interator.PaymentOptionInterator;
import com.example.loginapp.model.listener.PaymentOptionListener;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.payment_option.PaymentOptionView;

public class PaymentOptionPresenter implements PaymentOptionListener {
    private final PaymentOptionView view;

    private final PaymentOptionInterator interator = new PaymentOptionInterator(this);

    private boolean isSaveDeliveryAddress;

    private boolean isProductsFroShoppingCart;

    private final int shippingCost = Constant.ShippingCost;

    private Order order;

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        order.setPaymentMethod(paymentMethod);
    }

    public PaymentOptionPresenter(PaymentOptionView view) {
        this.view = view;
    }

    public void setSaveDeliveryAddress(boolean saveDeliveryAddress) {
        isSaveDeliveryAddress = saveDeliveryAddress;
    }

    public void setProductsFroShoppingCart(boolean productsFroShoppingCart) {
        isProductsFroShoppingCart = productsFroShoppingCart;
    }

    public void setOrder(Order order) {
        this.order = order;
        double subTotal = order.getOrderProducts().stream().mapToInt(product -> product.getPrice() * product.getQuantity()).sum();
        Voucher voucher = order.getVoucher();
        if (voucher != null) {
            if (voucher.getVoucherType() == VoucherType.Discount) {
                subTotal = (int) (subTotal - ((subTotal * voucher.getDiscountPercentage()) / 100));
            }
            if (voucher.getVoucherType() == VoucherType.FreeShipping) {
                subTotal -= shippingCost;
            }
        }
        double total = subTotal + shippingCost;
        total = Math.round(total * 100.00) / 100.00;
        view.setView(String.valueOf(subTotal), String.valueOf(shippingCost), String.valueOf(total));
        order.setPaymentTotal(total);
    }

    public void createOrder() {
        view.isLoading(true);
        interator.createOrder(order, isSaveDeliveryAddress, isProductsFroShoppingCart);
    }

    @Override
    public void goOrderSuccessScreen() {
        view.isLoading(false);
        view.goOrderSuccessScreen();
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }
}
