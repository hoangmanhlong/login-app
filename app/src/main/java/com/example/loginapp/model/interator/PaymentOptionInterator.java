package com.example.loginapp.model.interator;

import com.example.loginapp.utils.Constant;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.PaymentOptionListener;

public class PaymentOptionInterator {

    private final PaymentOptionListener listener;

    public PaymentOptionInterator(PaymentOptionListener listener) {
        this.listener = listener;
    }

    public void createOrder(Boolean saveAddress, Order order, int total, PaymentMethod paymentMethod, Boolean isCart, Voucher voucher) {
        Order newOrder;
        if (voucher == null) newOrder = new Order(order.getOrderProducts(), order.getDeliveryAddress(), paymentMethod, total);
        else newOrder = new Order(order.getOrderProducts(), order.getDeliveryAddress(), paymentMethod, total, voucher);

        Constant.orderRef
                .child(Constant.currentUser.getUid())
                .child(newOrder.getOrderId())
                .setValue(newOrder)
                .addOnCompleteListener(task -> listener.goOrderSuccessScreen())
                .addOnFailureListener(task -> listener.onMessage("An error occurred, Please try again"));

        if (saveAddress)
            Constant.deliveryAddressRef
                    .child(Constant.currentUser.getUid())
                    .child(order.getDeliveryAddress().getDeliveryAddressId())
                    .setValue(order.getDeliveryAddress());

        if (isCart)
            for (OrderProduct product : order.getOrderProducts())
                Constant.cartRef
                        .child(Constant.currentUser.getUid())
                        .child(String.valueOf(product.getId()))
                        .removeValue();
    }
}
