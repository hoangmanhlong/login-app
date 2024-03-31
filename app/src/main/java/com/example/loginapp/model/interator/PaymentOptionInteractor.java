package com.example.loginapp.model.interator;

import android.util.Log;

import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.listener.PaymentOptionListener;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.payment_option.PaymentOptionMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

public class PaymentOptionInteractor {

    private static final String TAG = PaymentOptionInteractor.class.getSimpleName();

    private PaymentOptionListener listener;

    private FirebaseUser user;

    public PaymentOptionInteractor(PaymentOptionListener listener) {
        this.listener = listener;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void createOrder(Order order, boolean isSaveDeliveryAddress, boolean isProductsFroShoppingCart) {

        String uid = user.getUid();

        if (isSaveDeliveryAddress)
            Constant.deliveryAddressRef
                    .child(uid)
                    .child(order.getDeliveryAddress().getDeliveryAddressId())
                    .setValue(order.getDeliveryAddress());

        if (isProductsFroShoppingCart)
            for (OrderProduct product : order.getOrderProducts())
                Constant.cartRef
                        .child(uid)
                        .child(String.valueOf(product.getId()))
                        .removeValue();

        Voucher voucher  = order.getVoucher();
        if (voucher != null) {
            Constant.myVoucherRef.child(uid).child(voucher.getVoucherCode()).removeValue();
            EventBus.getDefault().postSticky(new PaymentOptionMessage());
        }


        order.setOrderId("SA" + System.currentTimeMillis());
        order.setOrderDate(System.currentTimeMillis());
        order.setOrderStatus(OrderStatus.Processing);

        Constant.orderRef
                .child(uid)
                .child(order.getOrderId())
                .setValue(order)
                .addOnCompleteListener(task -> listener.goOrderSuccessScreen())
                .addOnFailureListener(e -> Log.d(TAG, "createOrder: " + e.getMessage()));
    }

    public void removePaymentOptionListener() {
        listener = null;
        user = null;
    }
}
