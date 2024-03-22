package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;
import com.example.loginapp.model.interator.PaymentOptionInterator;
import com.example.loginapp.model.listener.PaymentOptionListener;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.payment_option.PaymentOptionView;

import org.greenrobot.eventbus.EventBus;

public class PaymentOptionPresenter implements PaymentOptionListener {

    private final PaymentOptionView view;

    private final PaymentOptionInterator interator = new PaymentOptionInterator(this);

    private boolean isSaveDeliveryAddress;

    private boolean isProductsFromShoppingCart;

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

    public void setProductsFromShoppingCart(boolean productsFroShoppingCart) {
        isProductsFromShoppingCart = productsFroShoppingCart;
    }

    public void setOrder(Order order) {
        this.order = order;
        double reducedPrice = 0.0d;
        double merchandiseSubtotal = order.getOrderProducts().stream().mapToInt(product -> product.getPrice() * product.getQuantity()).sum();
        Voucher voucher = order.getVoucher();
        int shippingCost = Constant.ShippingCost;
        double paymentTotal = merchandiseSubtotal + shippingCost;
        if (voucher != null) {
            view.hasVoucher(true);
            if (voucher.getVoucherType() == VoucherType.FreeShipping) {
                reducedPrice = 200;
            }
            if (voucher.getVoucherType() == VoucherType.Discount) {
                reducedPrice = (paymentTotal * voucher.getDiscountPercentage() / 100);
            }
        } else {
            view.hasVoucher(false);
            reducedPrice = 0;
        }

        // Làm tròn reducedPrice đến chữ số thứ hai sau dấu phẩy
        reducedPrice = Math.round(reducedPrice * 100.0) / 100.0;

        paymentTotal -= reducedPrice;
        order.setPaymentTotal(paymentTotal);
        view.setView(String.valueOf(merchandiseSubtotal), String.valueOf(shippingCost), String.valueOf(reducedPrice), String.valueOf(paymentTotal));
    }

    public void createOrder() {
        view.isLoading(true);
        interator.createOrder(order, isSaveDeliveryAddress, isProductsFromShoppingCart);
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
