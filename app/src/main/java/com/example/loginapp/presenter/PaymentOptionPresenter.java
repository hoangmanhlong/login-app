package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;
import com.example.loginapp.model.interator.PaymentOptionInterator;
import com.example.loginapp.model.listener.PaymentOptionListener;
import com.example.loginapp.view.fragment.payment_option.PaymentOptionView;

public class PaymentOptionPresenter implements PaymentOptionListener {
    private final PaymentOptionView view;

    private final PaymentOptionInterator interator = new PaymentOptionInterator(this);

    private Boolean isCart;

    private int total = 0;

    private Boolean isSave;

    private int shippingCost = 200;

    private PaymentMethod paymentMethod;

    private Order order;

    private Voucher voucher;

    public void setCart(Boolean cart) {
        isCart = cart;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentOptionPresenter(PaymentOptionView view) {
        this.view = view;
    }

    public void getOrder(Order order) {
        this.order = order;
        int subTotal = order.getOrderProducts().stream().mapToInt(product -> product.getPrice() * product.getQuantity()).sum();
        voucher = order.getVoucher();
        if (voucher != null) {
            if (voucher.getVoucherType() == VoucherType.Discount) {
                subTotal = (int) (subTotal - ((subTotal * voucher.getDiscountPercentage()) / 100));
            } else if (voucher.getVoucherType() == VoucherType.FreeShipping) {
                shippingCost = 0;
            }
            total = subTotal + shippingCost;
        } else {
            total = subTotal + shippingCost;
        }
        view.setView(String.valueOf(subTotal), String.valueOf(shippingCost), String.valueOf(total));
    }

    public void createOrder() {
        view.isLoading(true);
        interator.createOrder(isSave, order, total, paymentMethod, isCart, voucher);
    }

    public void updateSaveDeliveryAddressState(Boolean save) {
        isSave = save;
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
