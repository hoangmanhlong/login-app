package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.model.entity.Order;
import com.example.loginapp.model.entity.OrderStatus;
import com.example.loginapp.model.entity.PaymentMethod;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.entity.VoucherType;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.buy_again.BuyAgainView;
import com.google.firebase.auth.FirebaseAuth;

public class BuyAgainPresenter {

    private final BuyAgainView view;

    private Order currentOrder;

    private int merchandiseSubtotal = 0;

    private double reducedPrice = 0.0;

    private int shippingCost = Constant.ShippingCost;

    private double total = 0;

    public BuyAgainPresenter(BuyAgainView view) {
        this.view = view;
    }

    public void initData() {
        if (currentOrder == null) view.getSharedData();
        else bindDataToView(currentOrder);
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        currentOrder.setDeliveryAddress(deliveryAddress);
        view.bindAddress(deliveryAddress);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        currentOrder.setPaymentMethod(paymentMethod);
        view.bindPaymentMethod(paymentMethod);
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
        currentOrder.setVoucher(null);
        view.hasVoucher(false);
        bindDataToView(currentOrder);
    }

    public void checkout() {
        view.isLoading(true);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentOrder.setOrderId("SA" + System.currentTimeMillis());
        currentOrder.setOrderStatus(OrderStatus.Processing);
        currentOrder.setOrderDate(System.currentTimeMillis());
        currentOrder.setPaymentTotal(total);

        Voucher voucher = currentOrder.getVoucher();
        if (voucher != null) {
            Constant.myVoucherRef.child(uid)
                    .child(voucher.getVoucherCode()).removeValue()
                    .addOnFailureListener(e -> view.onCheckoutSuccess(false));
        }

        Constant.orderRef.child(uid)
                .child(currentOrder.getOrderId())
                .setValue(currentOrder)
                .addOnCompleteListener(task -> view.onCheckoutSuccess(task.isSuccessful()))
                .addOnFailureListener(e -> view.onCheckoutSuccess(false));
    }

    public void setVoucher(Voucher voucher) {
        currentOrder.setVoucher(voucher);
        view.hasVoucher(true);
        bindDataToView(currentOrder);
    }

    private void bindDataToView(Order order) {
        view.bindAddress(order.getDeliveryAddress());
        view.bindOrderProducts(order.getOrderProducts());
        view.bindPaymentMethod(order.getPaymentMethod());
        merchandiseSubtotal = order.getOrderProducts().stream()
                .mapToInt(product -> product.getPrice() * product.getQuantity()).sum();
        view.bindMerchandiseSubtotal(String.valueOf(merchandiseSubtotal));
        view.bindShippingCost(String.valueOf(shippingCost));
        Voucher voucher = order.getVoucher();
        total = merchandiseSubtotal + shippingCost;
        if (voucher != null) {
            view.bindVoucherCode(order.getVoucher().getVoucherCode());
            if (voucher.getVoucherType() == VoucherType.FreeShipping) {
                reducedPrice = 200;
            }
            if (voucher.getVoucherType() == VoucherType.Discount) {
                reducedPrice = (total * voucher.getDiscountPercentage() / 100);
            }
        } else {
            reducedPrice = 0;
        }

        // Làm tròn reducedPrice đến chữ số thứ hai sau dấu phẩy
        reducedPrice = Math.round(reducedPrice * 100.0) / 100.0;

        total -= reducedPrice;
        view.bindReducedPrice(String.valueOf(reducedPrice));
        view.bindTotalPayment(String.valueOf(total));
    }
}
